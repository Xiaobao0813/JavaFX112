package mypos;

import java.util.TreeMap;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import models.OrderDetail;
import models.Product;
import models.ReadCategoryProduct;

public class App extends Application {

    private TilePane menuArrange_meal = getProductCategoryMenu("排餐");
    TilePane menuSnack = getProductCategoryMenu("點心");
    TilePane menuDrinks = getProductCategoryMenu("飲料");

    private ObservableList<OrderDetail> order_list;
    private TableView<OrderDetail> table;

    private final TreeMap<String, Product> product_dict = ReadCategoryProduct.readProduct();

    private final TextArea display = new TextArea();

    public TilePane getProductCategoryMenu(String category) {
        TreeMap<String, Product> product_dict = ReadCategoryProduct.readProduct();
        TilePane category_menu = new TilePane();
        category_menu.setVgap(10);
        category_menu.setHgap(10);
        category_menu.setPrefColumns(4);

        for (String item_id : product_dict.keySet()) {
            if (product_dict.get(item_id).getCategory().equals(category)) {
                Button btn = new Button();
                btn.setPrefSize(120, 120);

                Image img = new Image("/imgs/" + product_dict.get(item_id).getPhoto());
                ImageView imgview = new ImageView(img);
                imgview.setFitHeight(100);
                imgview.setFitWidth(100);
                imgview.setPreserveRatio(true);
                btn.setGraphic(imgview);
                category_menu.getChildren().add(btn);

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        addToCart(item_id);
                        System.out.println(product_dict.get(item_id).getName());
                    }
                });
            }
        }
        return category_menu;
    }

    VBox menuContainerPane = new VBox();

    public TilePane getMenuSelectionContainer() {
        Button btnArrange_meal = new Button();
        btnArrange_meal.setText("排餐");
        btnArrange_meal.setStyle("-fx-background-color: lightgreen;");
        btnArrange_meal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                menuContainerPane.getChildren().clear();
                menuContainerPane.getChildren().add(menuArrange_meal);
            }
        });
        Button btnSnack = new Button("點心");
        btnSnack.setStyle("-fx-background-color: lightgreen;");
        btnSnack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                select_category_menu(e);
            }
        });
        Button btnDrinks = new Button("飲料");
        btnDrinks.setStyle("-fx-background-color: lightgreen;");
        btnDrinks.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                select_category_menu(e);
            }
        });

        TilePane conntainerCategoryMenuBtn = new TilePane();
        conntainerCategoryMenuBtn.setVgap(10);
        conntainerCategoryMenuBtn.setHgap(10);

        conntainerCategoryMenuBtn.getChildren().add(btnArrange_meal);
        conntainerCategoryMenuBtn.getChildren().add(btnSnack);
        conntainerCategoryMenuBtn.getChildren().add(btnDrinks);

        return conntainerCategoryMenuBtn;
    }

    public void select_category_menu(ActionEvent event) {
        String category = ((Button) event.getSource()).getText();
        menuContainerPane.getChildren().clear();
        switch (category) {
            case "排餐":
                menuContainerPane.getChildren().add(menuArrange_meal);
                break;
            case "點心":
                menuContainerPane.getChildren().add(menuSnack);
                break;
            case "飲料":
                menuContainerPane.getChildren().add(menuDrinks);
                break;
            default:
                break;
        }
    }

    public TilePane getOrderOperationContainer() {
        Button btnCombo = new Button();
        btnCombo.setText("人氣套餐");
        btnCombo.setStyle("-fx-background-color: yellow;");
        btnCombo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addToCart("p-a-101");
                addToCart("p-s-108");
                addToCart("p-d-112");
            }
        });

        Button btnDelete = new Button("刪除一筆");
        btnDelete.setStyle("-fx-background-color: red;");
        btnDelete.setOnAction((ActionEvent e) -> {
            OrderDetail selectedItem = table.getSelectionModel().getSelectedItem();
            order_list.remove(selectedItem);
            checkTotal();
        });
        // 新增结帳按钮
        Button btnCheckout = new Button("我要結帳");
        btnCheckout.setStyle("-fx-background-color: lightblue;");
        btnCheckout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int total = calculateTotal();
                order_list.clear();
                table.refresh();
                display.setText("結帳完成!\n總金額: " + total);
            }
        });

        TilePane operationBtnTile = new TilePane();
        operationBtnTile.setVgap(10);
        operationBtnTile.setHgap(10);

        operationBtnTile.getChildren().add(btnCombo);
        operationBtnTile.getChildren().add(btnDelete);
        operationBtnTile.getChildren().add(btnCheckout);

        return operationBtnTile;

    }

    public void initializeOrderTable() {
        order_list = FXCollections.observableArrayList();
        checkTotal();

        table = new TableView<>();
        table.setEditable(true);
        table.setPrefHeight(300);

        TableColumn<OrderDetail, String> order_item_name = new TableColumn<>("品名");
        order_item_name.setCellValueFactory(new PropertyValueFactory("product_name"));
        order_item_name.setCellFactory(TextFieldTableCell.forTableColumn());
        order_item_name.setPrefWidth(100);
        order_item_name.setMinWidth(100);

        TableColumn<OrderDetail, Integer> order_item_price = new TableColumn<>("價格");
        order_item_price.setCellValueFactory(new PropertyValueFactory("product_price"));

        TableColumn<OrderDetail, Integer> order_item_qty = new TableColumn<>("數量");
        order_item_qty.setCellValueFactory(new PropertyValueFactory("quantity"));
        order_item_qty.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        order_item_qty.setOnEditCommit(event -> {
            int row_num = event.getTablePosition().getRow();
            int new_val = event.getNewValue();
            OrderDetail target = event.getTableView().getItems().get(row_num);
            target.setQuantity(new_val);
            checkTotal();
        });

        table.setItems(order_list);
        table.getColumns().addAll(order_item_name, order_item_price, order_item_qty);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void checkTotal() {
        double total = 0;
        for (OrderDetail od : order_list) {
            total += od.getProduct_price() * od.getQuantity();
        }
        String totalmsg = String.format("%s %d\n", "總金額:", Math.round(total));
        display.setText(totalmsg);
    }

    public int calculateTotal() {
        int total = 0;
        for (OrderDetail od : order_list) {
            total += od.getProduct_price() * od.getQuantity();
        }
        return total;
    }

    public void addToCart(String item_id) {
        boolean duplication = false;
        for (int i = 0; i < order_list.size(); i++) {
            if (order_list.get(i).getProduct_id().equals(item_id)) {
                int qty = order_list.get(i).getQuantity() + 1;
                order_list.get(i).setQuantity(qty);
                duplication = true;
                table.refresh();
                checkTotal();
                break;
            }
        }
        if (!duplication) {
            OrderDetail new_ord = new OrderDetail(
                    item_id,
                    product_dict.get(item_id).getName(),
                    product_dict.get(item_id).getPrice(),
                    1);
            order_list.add(new_ord);
            checkTotal();
        }
    }

    @Override
    public void start(Stage stage) {
        HBox root = new HBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.getStylesheets().add("/css/bootstrap3.css");

        VBox rightPane = new VBox();
        rightPane.setSpacing(10);

        TilePane menuSelectionTile = getMenuSelectionContainer();
        rightPane.getChildren().add(menuSelectionTile);

        menuContainerPane.getChildren().add(menuArrange_meal);
        rightPane.getChildren().add(menuContainerPane);

        VBox leftPane = new VBox();
        leftPane.setSpacing(10);

        leftPane.getChildren().add(getOrderOperationContainer());

        initializeOrderTable();
        leftPane.getChildren().add(table);

        display.setPrefColumnCount(10);
        leftPane.getChildren().add(display);

        root.getChildren().addAll(leftPane, rightPane);

        Scene scene = new Scene(root);
        stage.setTitle("牛排館點餐POS系統");
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
