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


@app.route('/', methods=['POST'])
def upload_video():
    
    # check if the post request has the file part
    if 'file' not in request.files:
        return 'no file :('
    
    # TODO: read other params

    # check if filename is set
    file = request.files['file']
    if file.filename == '':
        return 'no file name :('

    # make sure the file is a video
    if file and allowed_file(file.filename):
        filename = secure_filename(file.filename)
        file_location = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        file.save(file_location)
        video_id = uuid.uuid4().hex
        CONVERTED_VIDEOS[video_id] = filename

        subprocess.call(['python3 ./jumpcutter/jumpcutter.py --input_file {}'.format(file_location)], shell=True)

        return video_id
    else:
        return 'not video file :('


if __name__ == "__main__":
    if not os.path.exists(UPLOAD_FOLDER):
        os.mkdir(UPLOAD_FOLDER)
    app.run(host='0.0.0.0', port=80)
