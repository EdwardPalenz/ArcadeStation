package games.spaceinvaders.gameControllers;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.Texture;

import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class WallControll extends Component {
	
	private int impacts;
	private int endurance;
	
	public WallControll(int impacts) {
		this.impacts=0;
		endurance=impacts;
	}
	
	
	public void wallHit() {
		impacts++;
		Entities.animationBuilder().autoReverse(true).repeat(2).interpolator(Interpolators.RANDOM.EASE_IN())
		.duration(Duration.seconds(0.33)).scale(entity).to(new Point2D(1.2, 1.2)).buildAndPlay();
		
		if(impacts==endurance) {
			entity.getComponent(CollidableComponent.class).setValue(false);
			
			Entities.animationBuilder().interpolator(Interpolators.EXPONENTIAL.EASE_OUT()).duration(Duration.seconds(1))
			.onFinished(entity::removeFromWorld).translate(entity).from(entity.getPosition()).to(new Point2D(entity.getX(), FXGL.getAppHeight()+10))
			.buildAndPlay();
		}else if(impacts==endurance/2) {
			changeImage();
		}
	}
	
	
	public void changeImage() {
		Image image1=new Image("/games/spaceinvaders/main/resources/wall2.png",50,50,true,true);
		entity.setViewWithBBox(new Texture(image1));
	}
	
}
