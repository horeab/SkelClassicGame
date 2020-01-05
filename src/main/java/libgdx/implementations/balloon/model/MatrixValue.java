package libgdx.implementations.balloon.model;

public enum MatrixValue {

	//@formatter:off
	PLAYER_1                (1,  false, false, false, false, false, false, false,  null, null, "balloonp1"),
	PLAYER_2                (2,  false, false, false, false, false, false, false,  null, null, "balloonp2"),

	TORNADO_1_PLAYER_1      (11, true,  true,  false, false, false, true,  true,   null, null, "tornadoballoon1p1"),
	TORNADO_1_PLAYER_2      (22, true,  true,  false, false, false, true,  true,   null, null, "tornadoballoon1p2"),

	TORNADO_2_PLAYER_1      (111,true,  true,  false, false, false, false, false,  null, null, "tornadoballoon1p1"),
	TORNADO_2_PLAYER_2      (222,true,  true,  false, false, false, false, false,  null, null, "tornadoballoon1p2"),

	FINAL_PLAYER_1          (10, false, false, false, false, false, false, false,  null, null, "balloonp1"),
	FINAL_PLAYER_2          (20, false, false, false, false, false, false, false,  null, null, "balloonp2"),

	DESTROYED_PLAYER_1      (-1, false, false, false, false, false, false, false,  null, null, "destroyedballoonp1"),
	DESTROYED_PLAYER_2      (-2, false, false, false, false, false, false, false,  null, null, "destroyedballoonp2"),

	AIR                     (0,  true,  true,  false, false, false, false, false,  null, null, "air"),

	POINTS                  (3,  true,  true,  false, false, false, false, false,  10,   11,    "points"),
	ARROW_LEFT              (4,  false, false, false, true,  false, false, false,  9,    10,    "cloudleft"),
	ARROW_RIGHT             (5,  false, false, true,  false, false, false, false,  9,    10,    "cloudright"),
	PLANE                   (6,  false, false, false, false, true,  false, false,  5,    7,    "plane"),
	TORNADO                 (7,  true,  true,  false, false, false, true,  true,   6,    8,    "tornado"),
	BLOCKING_CLOUD          (8,  false, false, false, false, false, false, false,  4,    5,    "blockingcloud"),
	;
	//@formatter:on
	private int value;

	private boolean overlap;
	private boolean moveUp;
	private boolean moveRight;
	private boolean moveLeft;
	private boolean destroy;
	private boolean randomUp;
	private boolean moveOneStepWithPlayer;

	private Integer minPercent;
	private Integer maxPercent;

	private String imageName;

	private MatrixValue(int value, boolean overlap, boolean moveUp, boolean moveRight, boolean moveLeft, boolean destroy, boolean randomUp,
			boolean moveOneStepWithPlayer, Integer minPercent, Integer maxPercent, String imageName) {
		this.value = value;
		this.overlap = overlap;
		this.moveUp = moveUp;
		this.moveRight = moveRight;
		this.moveLeft = moveLeft;
		this.destroy = destroy;
		this.randomUp = randomUp;
		this.moveOneStepWithPlayer = moveOneStepWithPlayer;
		this.minPercent = minPercent;
		this.maxPercent = maxPercent;
		this.imageName = imageName;
	}

	public Integer getMinPercent() {
		return minPercent;
	}

	public int getMaxPercent() {
		return maxPercent;
	}

	public int getValue() {
		return value;
	}

	public boolean isOverlap() {
		return overlap;
	}

	public boolean isMoveUp() {
		return moveUp;
	}

	public boolean isMoveRight() {
		return moveRight;
	}

	public boolean isMoveLeft() {
		return moveLeft;
	}

	public String getImageName() {
		return imageName;
	}

	public static MatrixValue getMatrixValue(int value) {
		for (MatrixValue val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}

	public static boolean isDestroyed(int value) {
		return value == DESTROYED_PLAYER_1.value || value == DESTROYED_PLAYER_2.value;
	}

	public boolean isDestroy() {
		return destroy;
	}

	public boolean isRandomUp() {
		return randomUp;
	}

	public boolean isMoveOneStepWithPlayer() {
		return moveOneStepWithPlayer;
	}
}
