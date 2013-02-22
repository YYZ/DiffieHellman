/*
Author : Sam Halligan
*/

import java.util.Random;
import java.math.BigInteger;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

class crypto{

	//Prime Modulus p
	final static BigInteger primeModulus = new BigInteger("b59dd79568817b4b9f6789822d22594f376e6a9abc0241846de426e5dd8f6edd"
		+"ef00b465f38f509b2b18351064704fe75f012fa346c5e2c442d7c99eac79b2bc"
		+"8a202c98327b96816cb8042698ed3734643c4c05164e739cb72fba24f6156b6f"
		+"47a7300ef778c378ea301e1141a6b25d48f1924268c62ee8dd3134745cdf7323", 16);
	
	//Generator g
	final static BigInteger generator = new BigInteger("44ec9d52c8f9189e49cd7c70253c2eb3154dd4f08467a64a0267c9defe4119f2"
		+"e373388cfa350a4e66e432d638ccdc58eb703e31d4c84e50398f9f91677e8864"
		+"1a2d2f6157e2f4ec538088dcf5940b053c622e53bab0b4e84b1465f5738f5496"
		+"64bd7430961d3e5a2e7bceb62418db747386a58ff267a9939833beefb7a6fd68", 16);
		
	//Geoff's public key A
	final static BigInteger geoffPublicKey = new BigInteger("5af3e806e0fa466dc75de60186760516792b70fdcd72a5b6238e6f6b76ece1f1"
		+"b38ba4e210f61a2b84ef1b5dc4151e799485b2171fcf318f86d42616b8fd8111"
		+"d59552e4b5f228ee838d535b4b987f1eaf3e5de3ea0c403a6c38002b49eade15"
		+"171cb861b367732460e3a9842b532761c16218c4fea51be8ea0248385f6bac0d", 16);
	
	public static void main(String [] args) throws Exception{
		//Generate my private key and the shared key
		BigInteger privateKey = privateKeyGen();
		BigInteger publicKey = publicKeyGen(privateKey,generator,primeModulus);
		BigInteger sharedKey = sharedKeyGen(geoffPublicKey, primeModulus, privateKey);
		
		//Apply SHA-256 to the shared key to get a 256 bit digest k
		byte [] k = generateHash(sharedKey);
		
		//Using the digest k, encrypt a 128 bit block of 0's using AES in ECB mode, with no padding
		byte [] encoded = aesEncrypt(k);
		BigInteger cipherText = new BigInteger(1,encoded);
		
		//Print all of the values generated on this run of the program
		System.out.println("\nMy Public Key B = " + publicKey.toString(16));
		System.out.println("\nMy Private Key b = " + privateKey.toString(16));
		System.out.println("\nShared Key s = "+ sharedKey.toString(16));
		System.out.println("\nAES Encryption in ECB mode = " + cipherText.toString(16));
		
	}
	
	//A random 1023-bit BigInteger, my private key b
	static BigInteger privateKeyGen(){
		return new BigInteger(1023, new Random());
	}
	
	//My public key B -- g to the power of b (mod p)
	static BigInteger publicKeyGen(BigInteger privateKey, BigInteger generator, BigInteger primeMod){
		return modPow(generator, privateKey, primeMod);
	}
	
	//Shared key s
	static BigInteger sharedKeyGen(BigInteger geoffPubKey, BigInteger primeMod,BigInteger privateKey){
		return modPow(geoffPubKey, privateKey, primeMod);
	}
	
	//Modular exponentiation via squaring and multiplying
	static BigInteger modPow(BigInteger a, BigInteger x, BigInteger p){
		BigInteger answer = new BigInteger("1");
		BigInteger two = new BigInteger("2");
		
		while(x.compareTo(BigInteger.ZERO) > 0){
			if(x.mod(two).equals(BigInteger.ONE))
				answer = answer.multiply(a).mod(p);
			x = x.shiftRight(1);
			//square a and mod(p)
			a = a.multiply(a).mod(p);
		}
		return answer;
	}
	
	//Using SHA-256, generate a 256 bit key (k) from the shared key S, to be used as the key for AES encryption
	static byte[] generateHash(BigInteger sharedKey){
		byte [] digest = new byte[0];
		try{
			//Create a message digest object using SHA-256 and pass it the shared key
			MessageDigest m = MessageDigest.getInstance("SHA-256");
			byte[] x = sharedKey.toByteArray();
			m.update(x);
			digest = m.digest();
			}
		catch(Exception e){
			e.printStackTrace(System.err);
		}
		return digest;
	}
	//AES encryption
	static byte[] aesEncrypt(byte[] k){
		String m="";
		byte [] message = new byte[128];
		byte [] encryptedMessage = new byte[128];
		
		//Fill m with 0's
		for (int i=0;i<128;i++)
			m = m + "0";
				
		try{
			//Put m into a byte[]
			message = m.getBytes();
			
			//Create a SecretKeySpec object and pass in the 256 bit digest k as the key
			SecretKeySpec key = new SecretKeySpec(k,"AES");
			
			//Create the AES cipher and encrypt
			Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
			c.init(Cipher.ENCRYPT_MODE,key);
			encryptedMessage = c.doFinal(message);
		}
		catch(Exception  e){
			System.out.println(e);
		}
		return encryptedMessage;
	}
}
