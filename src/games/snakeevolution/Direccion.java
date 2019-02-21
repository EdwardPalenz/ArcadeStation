package games.snakeevolution;

public enum Direccion {
	DERECHA(SnakeEvolution.SNAKE_SIZE,0), IZQUIERDA(-SnakeEvolution.SNAKE_SIZE,0), ARRIBA(0,-SnakeEvolution.SNAKE_SIZE), ABAJO(0,SnakeEvolution.SNAKE_SIZE);
	
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
