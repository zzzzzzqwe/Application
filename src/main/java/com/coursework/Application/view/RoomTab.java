package com.coursework.Application.view;

import com.coursework.Application.service.RoomService;
import com.coursework.Application.util.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RoomTab {

    public static Tab createRoomsTab(TextArea outputArea) {
        Tab tab = new Tab("Аудитории");
        tab.getStyleClass().add("tab-label");

        VBox container = new VBox(15);
        container.getStyleClass().add("tab-content-area");
        container.setPadding(new Insets(15));

        HBox buttonsBox = new HBox(10);
        Button allRoomsBtn  = new Button("Все кабинеты");
        Button busyRoomsBtn = new Button("С преподавателем");
        Button freeRoomsBtn = new Button("Без преподавателя");
        allRoomsBtn.getStyleClass().add("action-button");
        busyRoomsBtn.getStyleClass().add("action-button");
        freeRoomsBtn.getStyleClass().add("action-button");
        buttonsBox.getChildren().addAll(allRoomsBtn, busyRoomsBtn, freeRoomsBtn);

        Separator separator1 = new Separator();

        Label headerAdd = new Label("Добавить аудиторию:");

        GridPane addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);

        Label roomNumberLabel = new Label("Номер:");
        TextField roomNumberField = new TextField();
        roomNumberField.setPromptText("Например, 101 или 302A");
        roomNumberField.setPrefWidth(250);
        roomNumberField.getStyleClass().add("text-field");

        Label floorLabel = new Label("Этаж:");
        TextField floorField = new TextField();
        floorField.setPromptText("1–4");
        floorField.setPrefWidth(250);
        floorField.getStyleClass().add("text-field");

        Label blockLabel = new Label("Блок:");
        TextField blockField = new TextField();
        blockField.setPromptText("Одна буква");
        blockField.setPrefWidth(250);
        blockField.getStyleClass().add("text-field");

        Label subjectLabel = new Label("Предмет:");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Название предмета");
        subjectField.setPrefWidth(250);
        subjectField.getStyleClass().add("text-field");

        Button addRoomBtn = new Button("Добавить");
        addRoomBtn.getStyleClass().add("action-button");
        addRoomBtn.setDisable(true);

        boolean isAdmin = "admin".equals(UserSession.getUsername());
        if (isAdmin) {
            addRoomBtn.setDisable(false);
            roomNumberField.setDisable(false);
            floorField.setDisable(false);
            blockField.setDisable(false);
            subjectField.setDisable(false);
        } else {
            roomNumberField.setDisable(true);
            floorField.setDisable(true);
            blockField.setDisable(true);
            subjectField.setDisable(true);
        }

        addForm.add(roomNumberLabel, 0, 0);
        addForm.add(roomNumberField, 1, 0);
        addForm.add(floorLabel,      0, 1);
        addForm.add(floorField,      1, 1);
        addForm.add(blockLabel,      0, 2);
        addForm.add(blockField,      1, 2);
        addForm.add(subjectLabel,    0, 3);
        addForm.add(subjectField,    1, 3);
        addForm.add(addRoomBtn,      1, 4);

        Separator separator2 = new Separator();

        Label deleteLabel = new Label("Удалить аудиторию:");

        GridPane deleteForm = new GridPane();
        deleteForm.setHgap(10);
        deleteForm.setVgap(10);
        Label roomDelLabel = new Label("Номер:");
        ComboBox<String> roomDelCombo = new ComboBox<>();
        roomDelCombo.setPrefWidth(250);
        roomDelCombo.getStyleClass().add("combo-field");
        roomDelCombo.getItems().add("Выберите аудиторию");
        roomDelCombo.getItems().addAll(RoomService.getAllRoomNumbers());
        roomDelCombo.getSelectionModel().select(0);

        Button deleteRoomBtn = new Button("Удалить");
        deleteRoomBtn.getStyleClass().add("action-button");

        if (isAdmin) {
            roomDelCombo.setDisable(false);
            deleteRoomBtn.setDisable(false);
        } else {
            roomDelCombo.setDisable(true);
            deleteRoomBtn.setDisable(true);
        }

        deleteForm.add(roomDelLabel,   0, 0);
        deleteForm.add(roomDelCombo,   1, 0);
        deleteForm.add(deleteRoomBtn,  1, 1);

        container.getChildren().addAll(
                buttonsBox,
                separator1,
                headerAdd,
                addForm,
                separator2,
                deleteLabel,
                deleteForm
        );

        addRoomBtn.setOnAction(e -> {
            String room    = roomNumberField.getText().trim();
            String floor   = floorField.getText().trim();
            String block   = blockField.getText().trim();
            String subject = subjectField.getText().trim();
            outputArea.setText(RoomService.addRoom(room, floor, block, subject));
        });

        deleteRoomBtn.setOnAction(e -> {
            String roomNumber = roomDelCombo.getValue().trim();
            String selectedRoom = roomDelCombo.getValue();
            if (selectedRoom == null || selectedRoom.equals("Выберите аудиторию")) {
                outputArea.setText("Ошибка: выберите аудиторию.");
                return;
            }
            outputArea.setText(RoomService.deleteRoom(roomNumber));
        });

        allRoomsBtn.setOnAction(e ->
                outputArea.setText(RoomService.getAllRoomsWithCurrentTeachers())
        );
        busyRoomsBtn.setOnAction(e ->
                outputArea.setText(RoomService.getOccupiedRooms())
        );
        freeRoomsBtn.setOnAction(e ->
                outputArea.setText(RoomService.getFreeRooms())
        );

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        double scrollMultiplier = 0.020;
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY() * scrollMultiplier;
            scrollPane.setVvalue(scrollPane.getVvalue() - delta);
            event.consume();
        });

        tab.setContent(scrollPane);
        tab.setClosable(false);

        return tab;
    }
}
