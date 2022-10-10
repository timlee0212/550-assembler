package  com.ece550.parsing;


import  com.ece550.assembulator.Assembulator;
import  com.ece550.instructions.BadInstructionException;
import  com.ece550.instructions.Instruction;

public class Parser {

	private static final int MAX_SHAMT = (1 << 5) -1 ;
	
	public static String parseLine(String line) throws BadInstructionException {
		
		// Ignore comments
		line = line.split("\\#")[0];
		
		// Split by commas
		String[] split_line = line.split("[,\\s]+");
		
		// Must have at least one command
		if(split_line.length < 1 || split_line[0].isEmpty()) {
			return "";
		}
		
		Instruction instr = Instruction.getByName(split_line[0]);
		
		switch (instr.type) {
		case R: 	return parseRType(split_line, instr);
		case I: 	return parseIType(split_line, instr);
		case JI: 	return parseJIType(split_line, instr);
		case JII: 	return parseJIIType(split_line, instr);
		case L:		return parseLType(split_line, instr);
		case NOOP: 	return toBinary(0, 32);
		default: 	throw new BadInstructionException(String.format("Unrecognized instruction type: %s\n", split_line[0]));
		}
		
	}
	
	
	static String parseLType(String[] splitLine, Instruction instr) throws BadInstructionException{
		if(instr == Instruction.WLI) {
			if (splitLine.length != 2) {
			    String message = String.format(BadInstructionException.MSG_TEMPLATE, "L",
                        getOriginalInstruction(splitLine), String.format(BadInstructionException.ARG_MSG, 2, splitLine.length-1));
			    throw new BadInstructionException(message);
			}
			
			String opcode = instr.getOpcode();
			String filler = toBinary(0, 19);
			int ascii = 0;
			try {
				ascii = Integer.valueOf(splitLine[1]);
			} catch (NumberFormatException nfe) {
				if	(splitLine[1].length() == 1) {
					char ch = splitLine[1].charAt(0);
					ascii = ch;
				} else {
				    throw new BadInstructionException(String.join(BadInstructionException.MSG_TEMPLATE, "L",
                            getOriginalInstruction(splitLine), String.format("Tried to parse %s to ASCII", nfe.getClass().getName())));
				}
			}
			
			String asciiCode = toBinary(ascii, 8);
			
			return opcode + filler + asciiCode;
		}

		if (instr == Instruction.WP) {
		    try {
                return instr.getOpcode() + toBinary(parseRegister(splitLine[1]), 5) + toBinary(0, 22);
            } catch (IllegalArgumentException e){
                String message = String.format(BadInstructionException.MSG_TEMPLATE, "L", getOriginalInstruction(splitLine),
                        e.getMessage());
                throw new BadInstructionException(message);
            }
		}

		String message = String.format(BadInstructionException.MSG_TEMPLATE, "L", getOriginalInstruction(splitLine),
                "Unsupported operation");
		throw new BadInstructionException(message);
	}
	
	static String parseJIIType(String[] splitLine, Instruction instr) throws BadInstructionException {
		if (splitLine.length != 2) {
			String message = String.format(BadInstructionException.MSG_TEMPLATE, "JII", getOriginalInstruction(splitLine),
                    String.format(BadInstructionException.ARG_MSG, 2, splitLine.length-1));
			throw new BadInstructionException(message);
		}
		
		String opcode = instr.getOpcode();
		
		String rd = splitLine[1];
		try {
            String rdCode = toBinary(parseRegister(rd), 5);
            return opcode + rdCode + toBinary(0, 22);
        } catch(IllegalArgumentException e){
		    String message = String.format(BadInstructionException.MSG_TEMPLATE, "JII", getOriginalInstruction(splitLine),
                    e.getMessage());
		    throw new BadInstructionException(message);
        }
	}
	
	static String parseJIType(String[] splitLine, Instruction instr) throws BadInstructionException {
		String opcode = instr.getOpcode();
		
		if (splitLine.length != 2) {
            String message = String.format(BadInstructionException.MSG_TEMPLATE, "JI", getOriginalInstruction(splitLine),
                    String.format(BadInstructionException.ARG_MSG, 2, splitLine.length-1));
		}
		
		String target = splitLine[1];
		String T;
		try {
			int tNum = Integer.valueOf(target);
			T = toBinary(tNum, 27);
		}catch (NumberFormatException nfe) {
			T = target;
		}
		
		return opcode + T;
	}
	
	static String parseIType(String[] splitLine, Instruction instr) throws BadInstructionException{
		
		boolean memInstr = 
				   instr == Instruction.LW 
				|| instr == Instruction.SW 
				|| instr == Instruction.SV
				|| instr == Instruction.LP;

		if (memInstr && splitLine.length != 3) {
		    String message = String.format(BadInstructionException.MSG_TEMPLATE, "I", getOriginalInstruction(splitLine),
                    String.format(BadInstructionException.ARG_MSG, 3, splitLine.length-1));
		    throw new BadInstructionException(message);
		}

		if(!memInstr && splitLine.length != 4){
            String message = String.format(BadInstructionException.MSG_TEMPLATE, "I", getOriginalInstruction(splitLine),
                    String.format(BadInstructionException.ARG_MSG, 4, splitLine.length-1));
            throw new BadInstructionException(message);
        }
		
		String arg2;
		String rd, rs, N;
		rd = splitLine[1];
		arg2 = splitLine[2];
		
		if (memInstr) {
			String[] addressCombo = arg2.split("[\\(\\)]");
			N = addressCombo[0];
			rs = addressCombo[1];
		} else {
			rs = arg2;
			N = splitLine[3];
		}
		
		String opcode = instr.getOpcode();
		if(N.matches("-?\\d+(\\.\\d+)?")) {
			try {
				String rsCode = toBinary(parseRegister(rs), 5);
				String rdCode = toBinary(parseRegister(rd), 5);
				String immediate = parseImmediate(N);
				return opcode + rdCode + rsCode + immediate;
			} catch (IllegalArgumentException e) {
				String message = String.format(BadInstructionException.MSG_TEMPLATE, "I", getOriginalInstruction(splitLine),
						e.getMessage());
				throw new BadInstructionException(message);
			}
		}
		else {
			try {
				String rsCode = toBinary(parseRegister(rs), 5);
				String rdCode = toBinary(parseRegister(rd), 5);
				return opcode + rdCode + rsCode + N;
			} catch (IllegalArgumentException e) {
				String message = String.format(BadInstructionException.MSG_TEMPLATE, "I", getOriginalInstruction(splitLine),
						e.getMessage());
				throw new BadInstructionException(message);
			}
		}

	}

    private static String getOriginalInstruction(String[] splitLine) {
        return String.join(", ", splitLine);
    }

    static String parseImmediate(String N) throws IllegalArgumentException{
		int value;
		
		try {
			value = Integer.valueOf(N);
		} catch ( NumberFormatException nfe) {
			throw new IllegalArgumentException(String.format("Illegal immediate value: %d\n", N));
		}

		return toBinary(value, 17);
	}
	
	/**
	 * Parses R type instructions
	 * 
	 * @param splitLine
	 * @param instr
	 * @return binary encoded R type instruction 
	 */
	static String parseRType(String[] splitLine, Instruction instr) throws BadInstructionException {

		if(splitLine.length != 4) {
            String message = String.format(BadInstructionException.MSG_TEMPLATE, "I", getOriginalInstruction(splitLine),
                    String.format(BadInstructionException.ARG_MSG, 4, splitLine.length-1));
            throw new BadInstructionException(message);
		}
		
		String rdArg = splitLine[1];
		String arg0 = splitLine[2];
		String arg1 = splitLine[3];
		
		// Get opcode
		String opcode = instr.getOpcode();
		String rdCode;
		String rsCode;
		try {
            rdCode = toBinary(parseRegister(rdArg), 5);
            rsCode = toBinary(parseRegister(arg0), 5);
        } catch(IllegalArgumentException e){
            String message = String.format(BadInstructionException.MSG_TEMPLATE, "R", getOriginalInstruction(splitLine),
                    e.getMessage());
            throw new BadInstructionException(message);
        }
		String rtCode = "00000";
		String shamt  = "00000";
		String aluCode = instr.getALUopcode();
		
		boolean shiftInstr = instr == Instruction.SRA || instr == Instruction.SLL || instr == Instruction.SRL;
		boolean isAddi = instr == Instruction.ADDI;
		if (!shiftInstr) {
		    try {
                rtCode = toBinary(parseRegister(arg1), 5);
            } catch (IllegalArgumentException e){
		        if(isAddi)
		            rtCode = toBinary(Integer.parseInt(arg1),5);
		        else {
                    String message = String.format(BadInstructionException.MSG_TEMPLATE, "R", getOriginalInstruction(splitLine),
                            e.getMessage());
                    throw new BadInstructionException(message);
                }
            }
		} else {
		    try {
                shamt = toBinary(parseShamt(arg1), 5);
            } catch(IllegalArgumentException e){
                String message = String.format(BadInstructionException.MSG_TEMPLATE, "R", getOriginalInstruction(splitLine),
                        e.getMessage());
                throw new BadInstructionException(message);
            }
		}
		
		return opcode + rdCode + rsCode + rtCode + shamt + aluCode + "00";
	}

	/**
	 * Parses Shamt string, returns 0 if bad
	 * 
	 * @param shamt
	 * @return
	 */
	static int parseShamt(String shamt) throws IllegalArgumentException{
		int value;
		
		try {
			value = Integer.valueOf(shamt);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException(String.format("Cannot parse %s to a numeric value\n", shamt));
		}
				
		if (value < 0 || MAX_SHAMT < value ) {
			throw new IllegalArgumentException(String.format("Shamt of %d is not within allowed range\n", value));
		}
		
		return value;
	}

	/**
	 * parses register, returns 0 if you pick a bad register value
	 * 
	 * @param regCode
	 * @return
	 */
	static int parseRegister(String regCode) throws IllegalArgumentException{

		if (!regCode.contains("$")) {
			throw new IllegalArgumentException(String.format("RegCode of %s is invalid: must start with $\n", regCode));
		}

		// Replace snow flake registers with equivalent
		if (regCode.equals("$rstatus")) {
			regCode = "30";
		} else if (regCode.equals("$ra")) {
			regCode = "31";
		}

		// Remove all non digit characters
		regCode = regCode.replaceAll("[\\$r]", "");

		Integer regNum;

		try {
			regNum =  Integer.valueOf(regCode);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException(String.format("RegCode of %s does not map to a 32-bit int value\n", regCode));
		}

		if (regNum < 0 || 32 <= regNum) {
			throw new IllegalArgumentException(String.format("Register value of %d does not map a register index between 0 and 31\n", regNum));
		}

		return regNum;
	}
	
	public static String toBinary(int b, int d) {
		String output = "";

		for (int i = d - 1; i >= 0; i--) {
			int masked = b >> i;
			output += (masked & 1);
		}

		return output;
	}
}
