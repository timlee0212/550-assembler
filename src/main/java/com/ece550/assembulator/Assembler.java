package com.ece550.assembulator;

import com.ece550.instructions.BadInstructionException;

import java.io.InputStream;
import java.io.OutputStream;

public interface Assembler {
		
	/**
	 * Writes mips assembly from a stream to a machine code stream
	 *
     * @param is Stream to read from
	 * @param os Stream to write to
	 */
	public void writeTo(InputStream is, OutputStream os, boolean pad) throws BadInstructionException;
}
