package  com.ece550.instructions;

public enum InstructionType {
	R,
	I,
	JI,
	JII,
	NOOP, 
	L;
	
	public static InstructionType getByName(String name) {
		for (InstructionType it : InstructionType.values()) {
			if (it.name() == name) {
				return it;
			}
		}
		
		return NOOP;
	}
}
