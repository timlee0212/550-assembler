package  com.ece550.instruction;

/**
 * Immutable structure for an instruction
 * 
 * @author ghb5
 */
public class Instruction {
	private String _Original;
	private String _Command, _Label;
	private int _Immediate, _Target;
	private int _RD, _RS, _RT;
	private int _PC;

	/**
	 * Constructor
	 * 
	 * 
	 */
	public Instruction(String original, int PC, String command, String label, int immediate, int target, int rd, int rs,
			int rt, int pc) {
		_Original = original;
		_Command = command;
		_Label = label;
		
		// Set Literals
		_Immediate = immediate;
		_Target = target;
		
		// Set register addresses
		_RD = rd;
		_RS = rs;
		_RT = rt;
		
		// Set Program count
		_PC = PC;

	}

	/**
	 * @return the original line
	 */
	public String original() {
		return new String(_Original);
	}

	/**
	 * @return the Program count at this line
	 */
	public int PC() {
		return _PC;
	}

	/**
	 * @return the command of this instruction
	 */
	public String command() {
		return new String(_Command);
	}

	/**
	 * @return the label for this line, null if it has no label
	 */
	public String label() {
		return new String(_Label);
	}

	/**
	 * @return the Immediate if there is one, null otherwise
	 */
	public int immediate() {
		return _Immediate;
	}

	/**
	 * @return the target if there is one, null otherwise
	 */
	public int target() {
		return _Target;
	}

	/**
	 * @return the rd
	 */
	public int RD() {
		return _RD;
	}

	/**
	 * @return the rs
	 */
	public int RS() {
		return _RS;
	}

	/**
	 * @return the rt
	 */
	public int RT() {
		return _RT;
	}
}