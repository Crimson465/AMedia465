
package amedia;

import java.awt.event.InputEvent;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {
    
    private MediaPlayer mediaPlayer;
    
    @FXML
    private MediaView mediaView;
    
    private String filePath;
    
    @FXML
    private Slider slider;
    
    @FXML
    private Slider seekSlider;
    
    @FXML
    private ToggleButton toggleVideo;
    
    @FXML
    private BorderPane bPane;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        
        
        List<String> ext = new ArrayList<String> ();
        ext.add("*.mp4");
        ext.add("*.m4a");
        ext.add("*.m4v");
        ext.add("*.mp3");
        ext.add("*.aif");
        ext.add("*.aiff");
        ext.add("*.m3u8");
        ext.add("*.fxm");
        ext.add("*.flv");
        ext.add("*.wav");
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a File supported by JavaFX", ext);
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        filePath = file.toURI().toString();
        
        if(filePath != null){
            
            if (mediaPlayer != null){
                mediaPlayer.stop();
            }
            
            Media media = new Media (filePath);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            
            DoubleProperty width = mediaView.fitWidthProperty();
            DoubleProperty height = mediaView.fitHeightProperty();
            
            width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
            
            slider.setValue(mediaPlayer.getVolume() * 100);
            slider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(slider.getValue()/100);
                }
            });
            
            
            /**
             * Gets the Time Length of the video file by finding the duration and converting to seconds
             * and uses that value to set the size of the slider bar
            */
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    //System.out.println();
                    seekSlider.setMax(media.getDuration().toSeconds());
                    seekSlider.setValue(newValue.toSeconds());
                }
            });
            
            /**
             * Sets the time slider bar to a specific value which allows users 
             * to click or drag the slider to the desired time position
             */
            seekSlider.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.pause();
                    mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
                    
                    seekSlider.setOnMouseReleased(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
                            mediaPlayer.play();
                            toggleVideo.setSelected(false);
                            toggleVideo.setText("Play");
                        }
                    });
                    
                }
            });
            
            mediaPlayer.play();
            
            toggleVideo.setSelected(false);
            toggleVideo.setText("Play");
        }
    }
    
    @FXML
    private void stopVideo(ActionEvent event){
        mediaPlayer.stop();
        toggleVideo.setSelected(true);
        toggleVideo.setText("Pause");
    }
    
    @FXML
    private void fastVideo(ActionEvent event){
        //mediaPlayer.setRate(mediaPlayer.getRate() + 0.25);
        mediaPlayer.seek(Duration.seconds(seekSlider.getValue() + 15));
    }
    
    @FXML
    private void fasterVideo(ActionEvent event){
        //mediaPlayer.setRate(mediaPlayer.getRate() + 0.5);
        mediaPlayer.seek(Duration.seconds(seekSlider.getValue() + 30));
    }
    
    @FXML
    private void slowVideo(ActionEvent event){
        //mediaPlayer.setRate(mediaPlayer.getRate() - 0.25);
        mediaPlayer.seek(Duration.seconds(seekSlider.getValue() - 15));
    }
    
    @FXML
    private void slowerVideo(ActionEvent event){
        //mediaPlayer.setRate(mediaPlayer.getRate() - 0.5);
        mediaPlayer.seek(Duration.seconds(seekSlider.getValue() - 30));
    }
    
    @FXML
    private void exitVideo(ActionEvent event){
        System.exit(0);
    }
    
    @FXML
    private void toggleVideo(ActionEvent event){
        toggleVideo.setSelected(toggleVideo.isSelected());
        System.out.println(toggleVideo.isSelected());
        
        if(toggleVideo.isSelected()){
            mediaPlayer.pause();
            toggleVideo.setText("Pause");
            System.out.println(toggleVideo.getText());
        }else if(!toggleVideo.isSelected()){
            mediaPlayer.play();
            toggleVideo.setText("Play");
            System.out.println(toggleVideo.getText());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
}


