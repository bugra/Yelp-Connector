package com.github.bugra.yelp_connector;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
public class MyWriter {
	public FileWriter outputText;
	public MyWriter(String nameOfFile) throws IOException{
		this.outputText = new FileWriter(nameOfFile, true);
	}
	public void writeStringList(List<String> stringList){
		BufferedWriter writer = new BufferedWriter(outputText);
		for(String item : stringList){
			try {
				writer.write(item+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
