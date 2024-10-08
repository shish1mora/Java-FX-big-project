package com.example.labadeniska;

import com.example.labadeniska.utils.DBHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Главный класс приложения, отвечающий за запуск и отображение интерфейса приложения
 */
public class MainApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    private static Connection connection;
    private static Stage mainStage;

    static Properties property = new Properties();
    {
        try (InputStream input = this.getClass().getResourceAsStream("/statements.properties")) {
            property.load(input);
        } catch (IOException e) {
            logger.error("Ошибка загрузки properties файла", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод вызывается при завершении работы приложения
     *
     * @throws Exception Исключение, которое может быть брошено
     */
    @Override
    public void stop() throws Exception {
        if (connection != null) {
            logger.info("Завершение работы приложения");
            super.stop();
        }
    }

    /**
     * Метод для получения подключения к базе данных
     *
     * @return Объект подключения к базе данных
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Метод для получения главного окна приложения
     * @return Объект главного окна
     */
    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * Метод для запуска главного окна приложения
     *
     * @param stage Объект окна приложения
     * @throws IOException Исключение ввода-вывода
     */
    @Override
    public void start(Stage stage) throws IOException {
        try {
            connection = DBHelper.getConnection();
            logger.info("Успешное подключение к базе данных");
        } catch (SQLException e) {
            logger.error("Ошибка подключения к базе данных", e);
            System.out.println("Ошибка подключение к базе данных! " + e.getMessage());
        }
        this.mainStage = stage;

        Locale.setDefault(Locale.ENGLISH);
        ResourceBundle bundle = ResourceBundle.getBundle(property.getProperty("lg.languages"), Locale.getDefault());

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setTitle("LabaDeniska");
        stage.setScene(scene);
        stage.show();

        logger.info("Главное окно приложения запущено");
    }

    /**
     * Метод для запуска приложения
     *
     * @param args Аргументы командной строки
     */
    public static void main(String[] args) {
        logger.info("Запуск приложения");
        launch();
    }
}