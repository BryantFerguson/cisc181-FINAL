package pkgUT;


import org.apache.poi.ss.formula.functions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import pkgLogic.Loan;

public class TestPMT {

	@Test
	public void TestPMT() {
		
		//	PMT is a standard function included in apache POI.
		//	For a given r (rate), n (number of payments), p (present value), f (future value), t (how compounding is applied)
		//	this function will determine payment
		
		//	This is an example with known values
		//	PMT returns with negative values (this is typical accounting).  
		
		double PMT;
		double r = 0.07 / 12;
		double n = 20 * 12;
		double p = 150000;
		double f = 0;
		boolean t = false;
		PMT = Math.abs(FinanceLib.pmt(r, n, p, f, t));		
		double PMTExpected = 1162.95;		
		assertEquals(PMTExpected, PMT, 0.01);
	}
	@Test
	public void TestLoanWithNoExtraPayment() {
		
		//This unit test should work with the values given
		double dLoanAmount = 50000;
		double dInterestRate = 0.07;
		int iNbrOfYears = 20;
		LocalDate localDate = LocalDate.now();
		double dAdditionalPayment = 0;
		double dEscrow = 0;

		Loan loan = new Loan(dLoanAmount, dInterestRate, iNbrOfYears, localDate, dAdditionalPayment, dEscrow, 0,0,0);

		assertTrue(loan.getLoanPayments().size() == 240);
		assertEquals(loan.getTotalPayments(), 93033.62, 0.01);
		assertEquals(loan.getTotalInterest(), 43035.87, 0.01);
	}
	
	@Test
	public void TestLoanWithExtraPayment() {
		
		//This unit test should work with the values given
		double dLoanAmount = 72000;
		double dInterestRate = 0.0359;
		int iNbrOfYears = 20;
		LocalDate localDate = LocalDate.now();
		double dAdditionalPayment = 300.00;
		double dEscrow = 0;

		Loan loan = new Loan(dLoanAmount, dInterestRate, iNbrOfYears, localDate, dAdditionalPayment, dEscrow,0,0,0);
		Loan Loan2 = new Loan(dLoanAmount, dInterestRate, iNbrOfYears, localDate, 0, dEscrow, 0, 0, 0);
        
	     
        assertTrue(loan.getLoanPayments().size() == 119);    
        assertEquals(loan.getTotalPayments(), 85658.72, 0.01);
        assertEquals(loan.getTotalInterest(), 13660.49, 0.01);
     

	}	
}

 

