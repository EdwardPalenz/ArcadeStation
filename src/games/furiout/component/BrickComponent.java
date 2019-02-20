package games.furiout.component;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.Entity;

public class BrickComponent extends Component {

    private int lives = 2;

    public void onHit() {
        FXGL.getAudioPlayer().playSound("brick_hit.wav");

        lives--;

        if (lives == 1) {
            entity.setView(FXGL.getAssetLoader().loadTexture("brick_blue_cracked.png", 232 / 3, 104 / 3));
        } else if (lives == 0) {
            entity.removeFromWorld();
        }
    }
}