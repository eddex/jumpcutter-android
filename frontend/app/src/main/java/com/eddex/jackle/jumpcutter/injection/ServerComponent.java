package com.eddex.jackle.jumpcutter.injection;

import com.eddex.jackle.jumpcutter.internet.ServerWrapper;

import dagger.Component;

@Component(modules = {ServerModule.class})
public interface ServerComponent {

    ServerWrapper provideServerWrapper();
}
