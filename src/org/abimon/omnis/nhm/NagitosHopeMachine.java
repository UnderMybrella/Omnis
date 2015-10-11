package org.abimon.omnis.nhm;

import java.io.File;

import org.abimon.omnis.io.Data;
import org.abimon.omnis.ludus.Ludus;

public class NagitosHopeMachine {
	
	public static void main(String[] args){
		Ludus.registerDataPool(NagitosHopeMachine.class.getClassLoader());
		Ludus.registerDataPool(new File("resources"));
		Data data = Ludus.getDataUnsafe("resources/GrassSpiral.png");
		System.out.println(data);
	}
}
