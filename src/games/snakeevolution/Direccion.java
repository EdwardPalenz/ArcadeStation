package games.snakeevolution;

public enum Direccion {
	DERECHA(Snake.SNAKE_SIZE,0), IZQUIERDA(-Snake.SNAKE_SIZE,0), ARRIBA(0,-Snake.SNAKE_SIZE), ABAJO(0,Snake.SNAKE_SIZE);
	
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
