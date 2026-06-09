module project.partb {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.mail;
    requires com.jfoenix;
    requires jdk.compiler;
    requires java.logging;

    opens project.partb to javafx.fxml;
    exports project.partb;
}