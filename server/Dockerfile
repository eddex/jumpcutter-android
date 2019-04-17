FROM ubuntu:18.04

WORKDIR /opt/

RUN apt update -y \
 && apt upgrade -y \
 && apt install -y python3-pip \
                   git \
                   ffmpeg \
 && git clone --recurse-submodules https://github.com/eddex/jumpcutter-android.git

WORKDIR /opt/jumpcutter-android/server

RUN pip3 install -r requirements.txt \
 && pip3 install -r jumpcutter/requirements.txt

ENTRYPOINT [ "python3" ]

CMD [ "app.py" ]
