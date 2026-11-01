package syncsquad.teamsync.controller;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import syncsquad.teamsync.commons.core.GuiSettings;
import syncsquad.teamsync.viewmodel.MainViewModel;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class TitleBarController extends UiPart<Region> {

    private static final String FXML = "TitleBar.fxml";

    private MainWindowController mainWindow;
    private ModalPane modalPane;

    @FXML
    private HBox titleBar;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button closeButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button maximizeButton;

    @FXML
    private MenuItem helpMenuItem;

    private HelpDialogController helpDialog;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public TitleBarController(MainWindowController mainWindowController, ModalPane modalPane) {
        super(FXML);

        this.mainWindow = mainWindowController;
        this.modalPane = modalPane;

        Stage primaryStage = mainWindow.getPrimaryStage();
        MainViewModel viewModel = mainWindow.getViewModel();

        this.helpDialog = new HelpDialogController(modalPane);

        viewModel.getIsShowingHelpProperty().addListener((unused1, oldValue, newValue) -> {
            if (newValue) {
                modalPane.show(helpDialog.getRoot());
                viewModel.getIsShowingHelpProperty().set(false);
            }
        });

        viewModel.getIsExiting().addListener((unused1, oldValue, newValue) -> {
            if (newValue) {
                handleExit();
            }
        });

        FontIcon closeIcon = new FontIcon(Material2AL.CLOSE);
        closeButton.setGraphic(closeIcon);
        closeButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT);

        FontIcon minimizeIcon = new FontIcon(Material2MZ.MINIMIZE);
        minimizeButton.setGraphic(minimizeIcon);
        minimizeButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT);
        minimizeButton.setOnAction(event -> primaryStage.setIconified(true));

        FontIcon maximizeIcon = new FontIcon(Material2AL.CHECK_BOX_OUTLINE_BLANK);
        FontIcon restoreIcon = new FontIcon(Material2AL.FILTER_NONE);
        maximizeButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT);
        maximizeButton.setGraphic(mainWindow.isMaximizedProperty().get() ? restoreIcon : maximizeIcon);
        maximizeButton.setOnAction(event -> mainWindow.setMaximized(!mainWindow.isMaximizedProperty().get()));

        mainWindow.isMaximizedProperty().addListener((unused1, oldValue, newValue) -> {
            if (newValue) {
                maximizeButton.setGraphic(restoreIcon);
            } else {
                maximizeButton.setGraphic(maximizeIcon);
            }
        });

        // Draggability
        this.titleBar.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        this.titleBar.setOnMouseDragged((MouseEvent event) -> {
            if (mainWindow.isMaximizedProperty().get()) {
                mainWindow.setMaximized(false);
                xOffset = primaryStage.getWidth() / 2;
            }
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        setAccelerators();
        // titleBar.setStyle("-fx-background-color: -color-accent-subtle");

    }

    void show() {
        Stage primaryStage = mainWindow.getPrimaryStage();
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        Stage primaryStage = mainWindow.getPrimaryStage();
        MainViewModel viewModel = mainWindow.getViewModel();
        double verticalDividerPosition = mainWindow.getVerticalDividerPosition();
        double horizontalDividerPosition = mainWindow.getHorizontalDividerPosition();
        boolean wasMaximized = mainWindow.isMaximizedProperty().get();
        mainWindow.setMaximized(false);
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY(),
                verticalDividerPosition, horizontalDividerPosition, wasMaximized);
        viewModel.saveGuiSettings(guiSettings);
        primaryStage.hide();
    }

    @FXML
    private void handleHelp() {
        if (modalPane.displayProperty().get()) {
            modalPane.hide();
        } else {
            modalPane.show(helpDialog.getRoot());
        }
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }
}
