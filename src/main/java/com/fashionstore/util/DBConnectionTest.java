package com.fashionstore.util;

import java.sql.Connection;

public class DBConnectionTest{
	public static void main(String[] args)
	{
		try(Connection connection=DBConnection.getConnection())
		{
			if(connection!=null)
			{
				System.out.println("DS");
			}
			else
			{
				System.out.println("No");
			}
		}
		catch(Exception e)
		{
			System.out.println("Failed");
			e.printStackTrace();
		}
	}
}
