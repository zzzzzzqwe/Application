package com.coursework.Application.view;

import com.coursework.Application.service.RoomService;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RoomTab {
    public static Tab createRoomsTab(TextArea outputArea) {
        Tab tab = new Tab("Аудитории");

        Button allRoomsBtn = new Button("Все");
        Button busyRoomsBtn = new Button("Занятые");
        Button freeRoomsBtn = new Button("Свободные");

        allRoomsBtn.setOnAction(e -> outputArea.setText(RoomService.getAllRoomsWithCurrentTeachers()));
        busyRoomsBtn.setOnAction(e -> outputArea.setText(RoomService.getOccupiedRooms()));
        freeRoomsBtn.setOnAction(e -> outputArea.setText(RoomService.getFreeRooms()));

        HBox buttons = new HBox(10, allRoomsBtn, busyRoomsBtn, freeRoomsBtn);
        buttons.setPadding(new Insets(10));

        VBox content = new VBox(buttons);
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }
}
