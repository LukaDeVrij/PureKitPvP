package me.lifelessnerd.purekitpvp.versioning;

import org.jetbrains.annotations.NotNull;

import static java.lang.Integer.parseInt;

public class Version implements Comparable<Version> {

    int major;
    int minor;
    int build;

    public Version(int major, int minor, int build) {
        this.major = major;
        this.minor = minor;
        this.build = build;
    }

    public Version(String versionString){
        // versionString: 1.20.6
        String[] split = versionString.split("\\.");
        this.major = parseInt(split[0]);
        this.minor = parseInt(split[1]);
        this.build = parseInt(split[2]);
    }

    public int getVersionNumber(){
        String versionNumber = major + String.valueOf(minor) + build; // hacky
        return parseInt(versionNumber);
    }

    @Override
    public int compareTo(@NotNull Version o) {
        // TODO
        return 0;
    }

}
