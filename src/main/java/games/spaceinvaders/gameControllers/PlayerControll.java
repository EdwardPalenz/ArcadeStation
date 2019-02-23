package games.spaceinvaders.gameControllers;

import com.almasb.fxgl.app.DSLKt;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


public class PlayerControll extends Component {
	
		
		private double dx=0;
		private double attackSpeed=2;
		private double playerMoveSpeed=300;
		
		private boolean canShoot=true;
		private double lastTimeShoot=0;
		
		
		@Override
		public void onUpdate(double timePerFrame) {
			dx=playerMoveSpeed*timePerFrame;
			
			if(!canShoot) {
				if((FXGL.getMasterTimer().getNow()-lastTimeShoot)>=1.0/attackSpeed) {
					canShoot=true;
				}
			}
		}
		
		public void left() {
			if(getEntity().getX()-dx>=0) {
				getEntity().translateX(-dx);
			}
		}
		
		public void right() {
			if(getEntity().getX()+getEntity().getWidth()+dx<=600) {
				getEntity().translateX(dx);
				
				
				
			
			}
			
		}
		
		public void shoot() {
			if(!canShoot) 
				return;
			
			
			canShoot=false;
			
			lastTimeShoot=FXGL.getMasterTimer().getNow();
			
			DSLKt.spawn("PlayerBullet",entity.getX()+7,entity.getY());
			
			DSLKt.play("shoot.wav");
			
			
			

		}
		
	
		
		public void playerHit() {
		
			entity.getComponent(CollidableComponent.class).setValue(false);
			PauseTransition delay=new PauseTransition(Duration.seconds(2));
			delay.setOnFinished(e->entity.getComponent(CollidableComponent.class).setValue(true));
			delay.play();
			
			
			
			
		}
		
	
	
	  
}
