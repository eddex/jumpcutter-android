import os
import uuid
import subprocess
from flask import Flask, request, redirect, url_for, send_from_directory
from werkzeug.utils import secure_filename
from pytube import YouTube

UPLOAD_FOLDER = '/tmp/upload'
ALLOWED_EXTENSIONS = set(['mp4', 'wmv', 'avi'])

UPLOADED_VIDEOS = {}
CONVERTED_VIDEOS = {}

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.route("/")
def hello():
    return '<p>Hello!</p><p>Uploaded videos: {}</p><p>Converted videos: {}</p>'.format(len(CONVERTED_VIDEOS), len(UPLOADED_VIDEOS))


@app.route('/youtube', methods=['GET'])
def use_youtube_video():

    url = request.args.get('url', None)
    if url is None:
        return 'error: no url param received :('

    video_id = uuid.uuid4().hex
    filename = 'youtube_{}'.format(video_id)
    try:
        YouTube(url) \
            .streams \
            .filter(subtype='mp4', progressive=True) \
            .order_by('resolution') \
            .first() \
            .download(app.config['UPLOAD_FOLDER'], filename=filename)
    except Exception as e:
        print (e)
        return 'error: can\'t parse youtube url :('
    UPLOADED_VIDEOS[video_id] = filename
    return video_id


@app.route('/upload', methods=['POST'])
def upload_video():
    
    # check if the post request has the file part
    if 'file' not in request.files:
        return 'error: no file :('

    # check if filename is set
    file = request.files['file']
    if file.filename == '':
        return 'error: no file name :('

    # make sure the file is a video
    if file and allowed_file(file.filename):
        filename = secure_filename(file.filename)
        video_id = uuid.uuid4().hex
        dotIndex = filename.rfind(".")
        filename = filename[:dotIndex]+"_"+str(video_id)+filename[dotIndex:]
        file_location = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        file.save(file_location)
        
        UPLOADED_VIDEOS[video_id] = filename

        return video_id
    else:
        return 'error: not a video file :('


def append_param(jumpcutter_command, param_name, param_value):
    '''
        append a command line parameter to the jumpcutter command if the value is set.
    '''
    if param_value is not None:
        jumpcutter_command = '{} --{} {}'.format(jumpcutter_command, param_name, param_value)
    return jumpcutter_command


@app.route('/process', methods=['GET'])
def process_video():
    '''
        processes the video and returns the download_id.
        example usage: https://server.ch?video_id=123&sounded_speed=2

        @params:
            video_id: the id of the video to process. returned by the upload route.
            
            silent_threshold: the volume amount that frames' audio needs to surpass to be consider 'sounded'. 
                              it ranges from 0 (silence) to 1 (max volume).
            sounded_speed: the speed that sounded (spoken) frames should be played at. Typically 1.
            silent_speed: the speed that silent frames should be played at. 999999 for jumpcutting.
            frame_margin: some silent frames adjacent to sounded frames are included to provide context. 
                          how many frames on either the side of speech should be included? That's this variable.
            sample_rate: sample rate of the input and output videos.
            frame_rate: frame rate of the input and output videos. optional... I try to find it out myself, but it doesn't always work.
            frame_quality: quality of frames to be extracted from input video. 1 is highest, 31 is lowest, 3 is the default.
    '''

    silent_threshold_name = 'silent_threshold'
    sounded_speed_name = 'sounded_speed'
    silent_speed_name = 'silent_speed'
    frame_margin_name = 'frame_margin'
    sample_rate_name = 'sample_rate'
    frame_rate_name = 'frame_rate'
    frame_quality_name = 'frame_quality'

    video_id  = request.args.get('video_id', None)
    if video_id is None:
        return 'error: no video_id param received :('

    silent_threshold  = request.args.get(silent_threshold_name, None)
    sounded_speed  = request.args.get(sounded_speed_name, None)
    silent_speed  = request.args.get(silent_speed_name, None)
    frame_margin  = request.args.get(frame_margin_name, None)
    sample_rate  = request.args.get(sample_rate_name, None)
    frame_rate  = request.args.get(frame_rate_name, None)
    frame_quality  = request.args.get(frame_quality_name, None)

    file_location = None
    try:
        file_location = os.path.join(app.config['UPLOAD_FOLDER'], UPLOADED_VIDEOS[video_id])
    except:
        return 'error: invalid video_id param received :('

    # create unique filename for converted video
    download_id = uuid.uuid4().hex
    original_filename = UPLOADED_VIDEOS[video_id]
    dotIndex = original_filename.rfind(".")
    converted_filename = original_filename[:dotIndex]+"_"+str(download_id)+original_filename[dotIndex:]
    CONVERTED_VIDEOS[download_id] = converted_filename

    converted_file_full_name = os.path.join(app.config['UPLOAD_FOLDER'], converted_filename)
    jumpcutter_command = 'python3 ./jumpcutter/jumpcutter.py --input_file {} --output_file {}'.format(file_location, converted_file_full_name)
    
    jumpcutter_command = append_param(jumpcutter_command, silent_threshold_name, silent_threshold)
    jumpcutter_command = append_param(jumpcutter_command, sounded_speed_name, sounded_speed)
    jumpcutter_command = append_param(jumpcutter_command, silent_speed_name, silent_speed)
    jumpcutter_command = append_param(jumpcutter_command, frame_margin_name, frame_margin)
    jumpcutter_command = append_param(jumpcutter_command, sample_rate_name, sample_rate)
    jumpcutter_command = append_param(jumpcutter_command, frame_rate_name, frame_rate)
    jumpcutter_command = append_param(jumpcutter_command, frame_quality_name, frame_quality)
    print (jumpcutter_command)
    
    subprocess.call(jumpcutter_command, shell=True)
    return download_id
        

@app.route('/download', methods=['GET'])
def download_video():
    download_id  = request.args.get('download_id', None)

    if download_id is not None:
        try:
            return send_from_directory(app.config['UPLOAD_FOLDER'], CONVERTED_VIDEOS[download_id])
        except:
            return 'error: invalid video_id param received :('
    else:
        return 'error: no download_id param received :('


if __name__ == "__main__":
    if not os.path.exists(UPLOAD_FOLDER):
        os.mkdir(UPLOAD_FOLDER)
    app.run(host='0.0.0.0', port=80)
