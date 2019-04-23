package turtle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

public class Grammar {
	
	Scanner sc; //Used to scan the file
	Turtle turtle = new DrawableTurtle(); //The turtle object which will draw the picture
	ArrayList<String> codeList = new ArrayList<String>(); //The ArrayList of code, line by line
	
	int InstructionPointer = 0; //Where the parser is at in the code
	
	Dictionary<String, Integer> variableTable = new Hashtable(); //A table of variables and their values
	
	public Grammar(File file) {
		try {
			sc = new Scanner(new File("testProgramStep3.txt"));
			
			//Add all the lines of the code to the codeList ArrayList to parse through
			while (sc.hasNextLine())
				codeList.add(sc.nextLine());
			
			//Go through the arrayList until the `programEnd` line is reached
			//TODO: Error handling for when `programEnd` isn't included and the Instruction pointer goes off the end of the array list
			while (!codeList.get(InstructionPointer).equals("programEnd")) {
				parseBlock();
				InstructionPointer++;
			}
			
		} catch (FileNotFoundException e) {
			// Error for when the file isn't found
			e.printStackTrace();
		}
		turtle.draw();
	}
	
	/**
	 * This function parses code blocks one at a time. The main code block encapsulating the whole program starts 
	 * with `begin` and ends with 'end'. All inner code blocks are preceded with a loop statement
	 */
	public void parseBlock() {
		
		//TODO: Error handling for when `end` isn't included and the Instruction pointer goes off the end of the array list
		
		//For each line of code until `end` is reached, parse whatever is on that line
		while (!codeList.get(InstructionPointer).trim().equals("end"))
			parseCommand(codeList.get(InstructionPointer));
		
	}
	
	/**
	 * Parses the command given.
	 * 
	 * @param text The line of code to parse
	 */
	public void parseCommand(String text) {
		String[] list = text.split("\\s"); //Split the line of code into sections to parse (//s is all whitespace)
		for (int i=0; i < list.length-1; i++) {
			int intValue;
			if (list[i].trim().length() > 0) {
				switch (list[i].trim()) {
					/** 
					 * Try/catch loops are needed to see if the value being assigned is a number or variable
					 * If it's a variable, it can pull the data from the variable table
					 */
				//TODO: Error handling for when a variable is passed that isn't part of the variable table
					case "loop":
						try {
							intValue = Integer.parseInt(list[i+1]);
						} catch (NumberFormatException e) {
							intValue = variableTable.get(list[i+1]);
						}
						System.out.println("Looping " + intValue + " times");
						parseLoop(intValue);
						break;
					case "forward":
						try {
							intValue = Integer.parseInt(list[i+1]);
						} catch (NumberFormatException e) {
							intValue = variableTable.get(list[i+1]);
						}
						turtle.forward(intValue);
						System.out.println("Moving forward: " + intValue);
						break;
					case "turn":
						try {
							intValue = Integer.parseInt(list[i+1]);
						} catch (NumberFormatException e) {
							intValue = variableTable.get(list[i+1]);
						}
						turtle.turn(intValue);
						System.out.println("Turning: " + intValue);
						break;
					case "=":
						break;
					default:
						System.out.println("Assigning: " + list[i] + " = " + list[i+2]);
						variableTable.put(list[i], Integer.parseInt(list[i+2]));
				}
			}
		}
		InstructionPointer++; //Move the instruction pointer to the next line of code
	}
	
	/**
	 * The loop method loops through the code block following it the number of times given.
	 * A copy of the instruction pointer is made so that the method can reset back to the beginning 
	 * of the code block each loop. 
	 * @param times The number of times the code block should be looped
	 */
	public void parseLoop(int times) {
		InstructionPointer++; //Move the instruction pointer to the beginning of the looped code block
		int startInstruction = InstructionPointer; //Create a copy of the current instruction pointer
		for (int i=0; i < times; i++){
			//Reset the instruction pointer to loop through the code again
			//Must do this at the beginning so that the program can continue normally at the end
			InstructionPointer = startInstruction;
			System.out.println("Loop #" + (i+1));
			parseBlock();
		}
	}
}
