package com.ece550.parsing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ece550.parsing.Parser;

import com.ece550.instructions.Instruction;

class ParserTest {
	
	Parser p;
	
	@BeforeEach
	void setUp() throws Exception {
		p = new Parser();
	}

	@Test
	void testParseLine() {
		Assertions.assertEquals("", Parser.parseLine(", , ,"));
		Assertions.assertEquals("", Parser.parseLine("# Just a comment"));
		
		String sw = "sw, $1, 256($r2) # a comment";
		String swCode = "00111" + "00001" + "00010" + "000"+ "00000100000000";
		Assertions.assertEquals(swCode, Parser.parseLine(sw));
		
		String sll = "sll $1 $31 31";
		String sllCode = "00000" + "00001" + "11111" + "00000" + "11111" + "00100" + "00";
		Assertions.assertEquals(sllCode, Parser.parseLine(sll));
		
		String noOp = "00000000000000000000000000000000";
		Assertions.assertEquals(noOp, Parser.parseLine("nop # wow # this is a comment"));
	
		String jal = "jal function";
		String jalCode = "00011" + "function";
		Assertions.assertEquals(jalCode, Parser.parseLine(jal));
	
		String badJr = "jr";
		Assertions.assertEquals("BAD JII", Parser.parseLine(badJr));
	}
	
	@Test
	void testParseJIIType() {
		String[] jr = new String[] {"jr", "$r20"};
		String jrCode = "00100" + "10100" + "000000" + "00000000" + "00000000";
		Assertions.assertEquals(jrCode, Parser.parseJIIType(jr, Instruction.JR));		
	}
	
	@Test
	void testParseJIType() {
		String[] j = new String[] {"j", "256"};
		String jCode = "00001" + "000" + "00000000" + "00000001" + "00000000";
		Assertions.assertEquals(jCode, Parser.parseJIType(j, Instruction.J));
		
		String[] badJ = new String[] {"j"};
		Assertions.assertEquals("BAD JI", Parser.parseJIType(badJ, Instruction.J));
	}
	
	@Test 
	void testParseIType() {
		// Test bad format mem instr
		Assertions.assertEquals("BAD I", Parser.parseIType(new String[] {"", "", "", ""}, Instruction.LW));
		
		// Test bad format addi
		Assertions.assertEquals("BAD I", Parser.parseIType(new String[] {"", "", ""}, Instruction.ADDI));
		
		String[] addi = new String[]{"addi", "$1", "$1", "1"};
		String addiCode = "00101" + "00001" + "00001" + "00000000000000001";
		Assertions.assertEquals(addiCode, Parser.parseIType(addi, Instruction.ADDI));
		
		String[] lw = new String[] {"lw", "$1", "-1($7)"};
		String lwCode = "01000" + "00001" + "00111" + "11111111111111111";
		Assertions.assertEquals(lwCode, Parser.parseIType(lw, Instruction.LW));
		
		String[] lp = new String[] {"lp", "$1", "0($1)"};
		String lpCode = "01011" + "00001" + "00001" + "00000000000000000";
		Assertions.assertEquals(lpCode, Parser.parseIType(lp, Instruction.LP));
	}
	
	@Test
	void testParseImmediate() {
		Assertions.assertEquals("BAD IMMEDIATE", Parser.parseImmediate("@#R")); 
		Assertions.assertEquals("00000000000000001", Parser.parseImmediate("1"));
	}
	
	@Test
	void testParseRType() {
		// Test wrong # arguments
		String[] small = new String[] {"", ""};
		Assertions.assertEquals("BAD R", Parser.parseRType(small, Instruction.ADD));
		
		// Test add
		String[] add_instr = new String[] {"add", "$0", "$0", "$0"};
		String add_code = "00000000" + "00000000" + "00000000" + "00000000";
		Assertions.assertEquals(add_code, Parser.parseRType(add_instr, Instruction.ADD));
	
		// Test shift instruction
		String[] sra_instr = new String[]{"sra", "$1", "$1", "2"};
		String sra_code = "00000" + "00001" + "00001" + "00000" + "00010" + "00101" + "00";
		Assertions.assertEquals(sra_code, Parser.parseRType(sra_instr, Instruction.SRA));
	}
	
	@Test 
	void testParseShamt() {
		// Check outside of bounds
		Assertions.assertEquals(0, Parser.parseShamt("-2"));
		Assertions.assertEquals(0, Parser.parseShamt("40"));
		
		// Check within bounds
		Assertions.assertEquals(20, Parser.parseShamt("20"));
		
		// Check bad format
		Assertions.assertEquals(0, Parser.parseShamt("wowza"));
	}
	
	@Test
	void testParseRegister() {
		// Test special case registers
		Assertions.assertEquals(30, Parser.parseRegister("$rstatus"), "Cannot parse rstatus");
		Assertions.assertEquals(31, Parser.parseRegister("$ra"), "Cannot parse return register");
		
		// Test normal case registers
		Assertions.assertEquals(5, Parser.parseRegister("$r5"), "Parse r5");
		Assertions.assertEquals(0, Parser.parseRegister("$r0"), "Parse r0");
		
		// Test abnormal input
		Assertions.assertEquals(0, Parser.parseRegister("21"));
		Assertions.assertEquals(0, Parser.parseRegister("This is not a register"));
		Assertions.assertEquals(0, Parser.parseRegister("woza0.1%2$#@%GFE#q4ga"));
		Assertions.assertEquals(0, Parser.parseRegister("$-1"));
		Assertions.assertEquals(0, Parser.parseRegister("$40"));
	}

	@Test
	void testToBinary() {
		// Test abnormal case
		Assertions.assertEquals("",   Parser.toBinary(20, 0));
		
		// Test positives
		Assertions.assertEquals("11", Parser.toBinary(3, 2));
		
		// Test negatives
		Assertions.assertEquals("11", Parser.toBinary(-1, 2));
		Assertions.assertEquals("11111111", Parser.toBinary(-1, 8));
	}
}
