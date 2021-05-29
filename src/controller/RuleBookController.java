package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class RuleBookController implements Initializable {
    @FXML
    private WebView webView;
    private WebEngine webEngine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
        URL urlRuleBook = getClass().getResource("/view/ScrabbleRules.xhtml");
        webEngine.load(urlRuleBook.toExternalForm());
    }
}
