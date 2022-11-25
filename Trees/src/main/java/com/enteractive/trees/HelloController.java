package com.enteractive.trees;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Контроллер
 */
public class HelloController implements Initializable {

    /**
     * Рабочая панель
     */
    public Pane workSpace;
    /**
     * Дерево иерархии
     */
    public TreeView<String> TreeBlocks;
    /**
     * Диалог для выбора файла
     */
    private final FileChooser fileChooser = new FileChooser();
    /**
     * Список блоков
     */
    BlockList blocks;
    /**
     * Блок, откуда идет соединение
     */
    Block from;
    /**
     * Блок, куда идет соединение
     */
    Block to;
    /**
     * Список стрелок
     */
    Arrows arrows;

    /** Метод для инициализации элементов интерфейса
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        blocks = new BlockList(this::drawArrows);
        arrows = new Arrows();
    }

    /** Отрисовка блока
     * @param shape блок, который будет отрисовываться
     */
    private void drawShape(Block shape) {
        workSpace.getChildren().add(shape);
        shape.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                workSpace.getChildren().remove(shape);
                blocks.getBlocks().remove(shape);
                drawArrows();
                TreeViewUpdate();
            }
            else if(mouseEvent.isControlDown())
                if(from == null)
                    from=shape;
                else if(to == null) {
                    to = shape;
                    drawArrow(from, to);
                    TreeViewUpdate();
                    from = null;
                    to = null;
                }
                else
                {
                    from=shape;
                    to = null;
                }
        });
        shape.draw();
    }

    /**
     * Создание нового блока
     */
    public void BlockCreate()
    {
        LocalDate data= LocalDate.now();
        Block block = blocks.addBlock(new Point2D(30,30),"Текст",false,TreeBlocks.layoutXProperty(),TreeBlocks.heightProperty(), data,"описание");
        drawShape(block);
        TreeViewUpdate();
    }

    /**
     * Обновление дерево иерархии
     */
    public void TreeViewUpdate()
    {
        blocks.parentDelete();
        TreeItem<String> blockTree = new TreeItem<>("Блоки");
        TreeBlocks.setRoot(blockTree);
        int main;
        boolean alone;
        for (Block block: blocks.getBlocks()) {
            main =0;
            alone=true;
            for (Arrow arrow : arrows.getArrowList()) {
                if (arrow.to == block) {main = 1; alone= false;}
                else if(arrow.from == block && main!=1) main=2;
                if (arrow.from == block) alone=false;
            }
            if (main==2 || alone){
                TreeItem<String> blockTree1 = new TreeItem<>(block.text);
                block.blockText.textProperty().addListener((observable, oldValue, newValue) ->blockTree1.setValue(newValue));
                blockTree.getChildren().add(blockTree1);
                if(main==2){
                    block.setParent(true);
                    TreeBlock(block,blockTree1);
                }
            }
        }
    }

    /** Рекурсивный метод для обновления дерева иерахий
     * @param block родительский блок
     * @param blockTree родительский элемент
     */
    public void TreeBlock(Block block,TreeItem<String> blockTree)
    {
        for (Arrow arrow : arrows.getArrowList()) {
            if (arrow.from == block && !arrow.to.parent)
            {
                TreeItem<String> childTree = new TreeItem<>(arrow.to.text);
                arrow.to.blockText.textProperty().addListener((observable, oldValue, newValue) ->childTree.setValue(newValue));
                blockTree.getChildren().add(childTree);
                TreeBlock(arrow.to,childTree);
            }
        }
    }

    /**
     * Отрисовка всех стрелок
     */
    private void drawArrows() {
        workSpace.autosize();

        ArrayList<ConnectedBlocks> temp = new ArrayList<>();
        for (Arrow arrow : arrows.getArrowList()) {
            if(blocks.getBlocks().contains(arrow.to) && blocks.getBlocks().contains(arrow.from))
                temp.add(new ConnectedBlocks(arrow.from, arrow.to));
            workSpace.getChildren().remove(arrow);
        }
        arrows.getArrowList().clear();

        for (ConnectedBlocks path : temp) {
            drawArrow(path.arr[0], path.arr[1]);
        }
    }

    /** Отрисовка  стрелки
     * @param from блок, откуда идет соединение
     * @param to блок, куда идет соединение
     */
    private void drawArrow(Block from, Block to) {
        var points = getConnectionPoints(from, to);
        Line line = new Line(points.item1.getX(), points.item1.getY(), points.item2.getX(), points.item2.getY());
        Arrow arrow = new Arrow(line,from,to);
        workSpace.getChildren().add(arrow);
        arrow.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                workSpace.getChildren().remove(arrow);
                arrows.getArrowList().remove(arrow);
                TreeViewUpdate();
            }
        });
        arrows.getArrowList().add(arrow);
    }

    /** Получить список точек для соединения блоков
     * @param from блок, откуда идет соединение
     * @param to блок, куда идет соединение
     * @return Точки для соединения блоков
     */
    private Points<Point2D, Point2D> getConnectionPoints(Block from, Block to) {
        var fromPoints = from.getArrayOfMinMaxPoints();
        var toPoints = to.getArrayOfMinMaxPoints();

        Point2D pointFromFinal = Point2D.ZERO;
        Point2D pointToFinal = Point2D.ZERO;
        double lowestDistance = Double.POSITIVE_INFINITY;

        for (Point2D fromPoint : fromPoints) {
            for (Point2D toPoint : toPoints) {
                var newDistance = fromPoint.distance(toPoint);
                if (newDistance < lowestDistance) {
                    pointFromFinal = fromPoint;
                    pointToFinal = toPoint;
                    lowestDistance = newDistance;
                }
            }
        }

        return new Points<>(pointFromFinal, pointToFinal);
    }

    /**
     * Инструкция
     */
    @FXML
    private void Instruction()
    {
        Alert info = new Alert(Alert.AlertType.INFORMATION, """
                Разместить блок - добавление блока,
                ПКМ по блоку/стрелке - удаление элемента,
                Ctrl + ЛКМ(по соединяемым блокам поочередно) - соединение блоков,
                Двойной клик по блоку - редактирование текста,
                Alt + ЛКМ - смена готовности элемента.
                """, ButtonType.CLOSE);
        info.setTitle("Информация");
        info.setHeaderText("Руководство");
        info.show();
    }

    /** Сохранить как
     * @throws IOException для файла
     */
    @FXML
    private void SaveAs() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите папку для сохранения файла");
        fileChooser.setInitialFileName("Дерево");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Дерево целей", "*.TreeDo"));
        File file = fileChooser.showSaveDialog(workSpace.getScene().getWindow());
        if (file != null) saveFile(file);
    }

    /** Сохранить файл
     * @param file сохраняемый файл
     * @throws IOException для файла
     */
    private void saveFile(File file) throws IOException {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream outStream = new ObjectOutputStream(fos);
            outStream.writeObject(blocks);
            outStream.writeObject(arrows);
            outStream.flush();
            outStream.close();
            System.out.println("Дерево целей сохранено!");
    }

    /** Открыть файл
     * @throws IOException для файла
     * @throws ClassNotFoundException для объектов
     */
    @FXML
    private void Open() throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл дерева целей");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Дерево целей", "*.TreeDo");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(workSpace.getScene().getWindow());
        if (file!=null)
            unpackingFile(file);
    }

    /** Открыть файл
     * @param file открываемый файл
     * @throws IOException для файла
     * @throws ClassNotFoundException для объектов
     */
    private void unpackingFile(File file) throws IOException, ClassNotFoundException {
        ClearAllObjects();
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream inputStream = new ObjectInputStream(fis);
        for (Block st: ((BlockList) inputStream.readObject()).getBlocks()) {
            Block temp = blocks.addBlock(new Point2D(st.pX,st.pY),st.text,st.ready,TreeBlocks.layoutXProperty(), TreeBlocks.heightProperty(), st.datacal,st.description );
            drawShape(temp);
        }
        for (Arrow arrow:((Arrows)inputStream.readObject()).getArrowList()) {
            for(Block connected:blocks.getBlocks()) {
                if (arrow.from.Equals(connected))
                    arrow.from = connected;
                if (arrow.to.Equals(connected))
                    arrow.to = connected;
            }
            drawArrow(arrow.from,arrow.to);
        }
        inputStream.close();
        TreeViewUpdate();
    }

    /**
     * Экспорт в пнг
     */
    @FXML
    private void ExportIntoPNG()
    {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Картинки (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            try {
                WritableImage snapShot = workSpace.snapshot(new SnapshotParameters(), null);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(snapShot, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    /**
     * Удалить все объекты
     */
    @FXML
    private void ClearAllObjects()
    {
        while (!arrows.getArrowList().isEmpty()){
            workSpace.getChildren().remove(arrows.getArrowList().get(0));
            arrows.getArrowList().remove(0);
        }
        while (!blocks.getBlocks().isEmpty()) {
            workSpace.getChildren().remove(blocks.getBlocks().get(0));
            blocks.getBlocks().remove(0);
        }
        TreeViewUpdate();
    }
}