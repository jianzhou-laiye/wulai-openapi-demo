using System;
using System.Net;
using System.IO;
using System.Text;
using System.Security.Cryptography;
using System.Collections.Generic;

public class Test
{
    private static string validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static Random random = new Random();

    private static string GenerateNonce(int length)
    {
        var nonceString = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            nonceString.Append(validChars[random.Next(0, validChars.Length - 1)]);
        }

        return nonceString.ToString();
    }

    public static string SHA1_Encrypt(string str)
    {
        var strRes = Encoding.Default.GetBytes(str);
        HashAlgorithm iSha = new SHA1CryptoServiceProvider();
        strRes = iSha.ComputeHash(strRes);
        var enText = new StringBuilder();
        foreach (byte iByte in strRes)
        {
            enText.AppendFormat("{0:x2}", iByte);
        }
        return enText.ToString();
    }


	public static void Main()
	{
    	var pubkey = "MY-KEY";
    	var secret = "MY-SECRET";
    	var nonce = GenerateNonce(32);
    	var timestamp = Convert.ToString((DateTime.Now.ToUniversalTime().Ticks - 621355968000000000) / 10000000);
    	var sign = SHA1_Encrypt(nonce + timestamp + secret);
    	Dictionary<string, string> headers = new Dictionary<string, string>();
    	headers.Add("Api-Auth-sign", sign);
    	headers.Add("Api-Auth-pubkey", pubkey);
    	headers.Add("Api-Auth-nonce", nonce);
    	headers.Add("Api-Auth-timestamp", timestamp);
    	foreach (KeyValuePair<string, string> kvp in headers)
        {
            Console.WriteLine("{0}: {1}", kvp.Key, kvp.Value);
        }
	}
}