# Jumpcutter Android
An Android app and a server for automatic video edits!

## Android app
The app lets the user select a video to edit and configure all settings needed by the server.

## Server
The server is used to edit the video by using [carykh's jumpcutter](https://github.com/carykh/jumpcutter) which is included as a submodule in this repository.

requires ffmpeg: `sudo apt install ffmpeg`

pull submodule: `git submodule init`

more info on submodules: https://gist.github.com/gitaarik/8735255

### using docker

build image: `jumpcutter-android/server$ docker build -t jumpcutter-server:latest .`

run container: `docker run -d -p 5000:5000 jumpcutter-server:latest`