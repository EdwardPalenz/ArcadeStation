package games.snakeClassic;

public enum Direccion {
	DERECHA(SnakeClassic.SNAKE_SIZE,0), IZQUIERDA(-SnakeClassic.SNAKE_SIZE,0), ARRIBA(0,-SnakeClassic.SNAKE_SIZE), ABAJO(0,SnakeClassic.SNAKE_SIZE);
	
	private int x;
	private int y;
	
	private Direccion(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
