module it.polimi.ingsw {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires java.desktop;
    requires junit;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.logging;
    requires java.rmi;

    exports it.polimi.ingsw to junit;
    exports it.polimi.ingsw.Client to junit;
    exports it.polimi.ingsw.Server to junit;
    exports it.polimi.ingsw.Network.Client.Socket;
    exports it.polimi.ingsw.Network.Client.RMI;
    exports it.polimi.ingsw.Server.Model.Enums to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.Client.Controller;
    exports it.polimi.ingsw.Server.Controller.Executor;
    exports it.polimi.ingsw.Server.Controller.MessagesToClient;
    exports it.polimi.ingsw.Client.Controller.MessagesToServer;
    exports it.polimi.ingsw.Client.Controller.Executor;
    exports it.polimi.ingsw.Client.View.CLI;
    exports it.polimi.ingsw.Client.View.GUI to javafx.controls, javafx.fxml, javafx.graphics;
    exports it.polimi.ingsw.Server.Model.Cards.CardCreators to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.Server.Model.Cards to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.Server.Model.Player;
    exports it.polimi.ingsw.Server.Model.Exceptions;
    exports it.polimi.ingsw.Server.Model.Board;
    exports it.polimi.ingsw.Network.Server.Connections;
    exports it.polimi.ingsw.Network.Client;
    exports it.polimi.ingsw.Server.Model;

    opens it.polimi.ingsw.Client.Controller to javafx.fxml, javafx.controls;
    opens it.polimi.ingsw to javafx.controls, javafx.fxml, javafx.graphics;
    opens it.polimi.ingsw.Server.Model.Cards to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.Server.Controller.MessagesToClient;
    opens it.polimi.ingsw.Client.Controller.MessagesToServer;
    opens it.polimi.ingsw.Client.View.GUI to javafx.controls, javafx.fxml, javafx.graphics;
    opens it.polimi.ingsw.Client to javafx.controls, javafx.fxml, javafx.graphics;
}