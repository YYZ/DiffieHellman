import java.util.Random;
import java.math.*;

//Class to generate my 1023 bit private key (b)
class privateKeyGen
{
	public static void main(String [] args)
	{
		BigInteger a = new BigInteger("4");
		BigInteger b = new BigInteger("13");
		BigInteger c = new BigInteger("1123123");
		
		System.out.println("mine "+ modPow(a,b,c));
		System.out.println("correct " + a.modPow(b,c));
	}
	
	static BigInteger modPow(BigInteger a, BigInteger x, BigInteger p){
		
	}
}