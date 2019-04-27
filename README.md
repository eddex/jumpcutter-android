# Jumpcutter Android
An Android app and a server for automatic video edits!

## Android app
The app lets the user select a video to edit and configure all settings needed by the server.

## Server
The server is used to edit the video by using [carykh's jumpcutter](https://github.com/carykh/jumpcutter) which is included as a submodule in this repository.

requires ffmpeg: `sudo apt install ffmpeg`

clone with submodule: `git clone --recurse-submodules https://github.com/eddex/jumpcutter-android.git`

more info on submodules: https://gist.github.com/gitaarik/8735255

### using docker

build image: `$ docker build -t jumpcutter-server:latest .` (in server directory)

run container: `$ docker run -d -p 80:80 jumpcutter-server:latest`

run with mapped upload folder: `docker run -dp 80:80 -v /<localpath>:/tmp/upload jumpcutter-server`
