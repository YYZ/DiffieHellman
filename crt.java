import java.math.BigInteger;
/*
Implementation of Chinese remainder theorem to perform modular expnonentation
ie. use crt to solve 8^12 (mod 167)
*/

class crt{
	public static void main(String [] args){
	
		byte [] message = new byte [128]; 
		System.out.println(message.toString());
	}
	
	static BigInteger modPow(BigInteger a, BigInteger x, BigInteger p){
		BigInteger answer = new BigInteger("1");
		BigInteger two = new BigInteger("2");
		
		while(x.compareTo(BigInteger.ZERO) > 0){
			if(x.mod(two).equals(BigInteger.ONE))
				answer = answer.multiply(a).mod(p);
			x = x.shiftRight(1);
			a = a.multiply(a).mod(p);
		}
		return answer;
	}
}