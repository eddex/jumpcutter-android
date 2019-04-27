import os
import uuid
import subprocess
from flask import Flask, request, redirect, url_for
from werkzeug.utils import secure_filename

UPLOAD_FOLDER = '/tmp/upload'
ALLOWED_EXTENSIONS = set(['mp4', 'wmv', 'avi'])

CONVERTED_VIDEOS = {}

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.route("/")
def hello():
    return 'Hello! Jumpcutter has converted {} videos!'.format(len(CONVERTED_VIDEOS))


@app.route('/upload', methods=['POST'])
def upload_video():
    
    # check if the post request has the file part
    if 'file' not in request.files:
        return 'error: no file :('
    
    # TODO: read other params

    # check if filename is set
    file = request.files['file']
    if file.filename == '':
        return 'error: no file name :('

    # make sure the file is a video
    if file and allowed_file(file.filename):
        filename = secure_filename(file.filename)
        file_location = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        file.save(file_location)
        video_id = uuid.uuid4().hex
        CONVERTED_VIDEOS[video_id] = filename

        return video_id
    else:
        return 'error: not a video file :('

def append_param(jumpcutter_command, param_name, param_value):
    '''
        append a command line parameter to the jumpcutter command if the value is set.
    '''

    if param_value not None:
        jumpcutter_command = '{} --{} {}'.format(jumpcutter_command, param_name, param_value)
    
    return jumpcutter_command

@app.route('/process', methods=['GET'])
def process_video():
    '''
        processes the video.
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
    silent_threshold  = request.args.get(silent_threshold, None)
    sounded_speed  = request.args.get(sounded_speed, None)
    silent_speed  = request.args.get(silent_speed, None)
    frame_margin  = request.args.get(frame_margin, None)
    sample_rate  = request.args.get(sample_rate, None)
    frame_rate  = request.args.get(frame_rate, None)
    frame_quality  = request.args.get(frame_quality, None)

    if video_id not None:
        file_location = os.path.join(app.config['UPLOAD_FOLDER'], CONVERTED_VIDEOS[video_id])
        jumpcutter_command = 'python3 ./jumpcutter/jumpcutter.py --input_file {}'.format(file_location)
        
        jumpcutter_command = append_param(jumpcutter_command, silent_threshold_name, silent_threshold)
        jumpcutter_command = append_param(jumpcutter_command, sounded_speed_name, sounded_speed)
        jumpcutter_command = append_param(jumpcutter_command, silent_speed_name, silent_speed)
        jumpcutter_command = append_param(jumpcutter_command, frame_margin_name, frame_margin)
        jumpcutter_command = append_param(jumpcutter_command, sample_rate_name, sample_rate)
        jumpcutter_command = append_param(jumpcutter_command, frame_rate_name, frame_rate)
        jumpcutter_command = append_param(jumpcutter_command, frame_quality_name, frame_quality)
        
        subprocess.call(jumpcutter_command, shell=True)
        return 'ok'
    else:
        return 'error: invalid or no video_id param received :('

@app.route('/download', methods=['GET'])
def download_video():
    pass

if __name__ == "__main__":
    if not os.path.exists(UPLOAD_FOLDER):
        os.mkdir(UPLOAD_FOLDER)
    app.run(host='0.0.0.0', port=80)
