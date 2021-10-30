package com.dimits.dimitsattendence.model;

public class StudentAttendanceModel {
    String name,id,attendance;

    public StudentAttendanceModel() {
    }

    public StudentAttendanceModel(String name, String id, String attendance) {
        this.name = name;
        this.id = id;
        this.attendance = attendance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
