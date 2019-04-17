import os
from flask import Flask, request, redirect, url_for

UPLOAD_FOLDER = '/tmp/upload'
ALLOWED_EXTENSIONS = set(['mp4', 'wmv', 'avi'])

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

@app.route("/")
def hello():
    return "Hello Jumpcutter!"

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

@app.route('/', methods=['POST'])
def upload_video():
    # check if the post request has the file part
    if 'file' not in request.files:
        return 'not a file :('
    file = request.files['file']
    # if user does not select file, browser also
    # submit a empty part without filename
    if file.filename == '':
        return 'no file name :('
    if file and allowed_file(file.filename):
        filename = file.filename
        file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        return 'ok :)'
    else:
        return 'not video file :('

if __name__ == "__main__":
    if not os.path.exists(UPLOAD_FOLDER):
        os.mkdir(UPLOAD_FOLDER)
    app.run(host='0.0.0.0', port=80)
