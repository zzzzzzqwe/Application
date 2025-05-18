package com.coursework.Application.view;

import com.coursework.Application.service.ScheduleService;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class ScheduleTab {

    public static Tab createScheduleTab(TextArea outputArea) {
        Tab tab = new Tab("Расписание");

        Button showScheduleBtn = new Button("Показать расписание");
        showScheduleBtn.setOnAction(e -> outputArea.setText(ScheduleService.getSchedule()));

        VBox content = new VBox(10, showScheduleBtn);
        content.setPadding(new Insets(10));
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }
}
