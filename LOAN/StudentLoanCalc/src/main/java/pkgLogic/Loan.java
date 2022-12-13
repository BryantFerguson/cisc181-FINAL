package pkgLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.formula.functions.FinanceLib;

public class Loan {
	private double LoanAmount;
	private double LoanBalanceEnd;
	private double InterestRate;
	private int LoanPaymentCnt;
	private boolean bCompoundingOption;
	private LocalDate StartDate;
	private double AdditionalPayment;
	private double Escrow;
	private HashMap<Integer, Double> hmRates = new HashMap<Integer, Double>();
	
	private ArrayList<Payment> loanPayments = new ArrayList<Payment>();

	public Loan(double loanAmount, double interestRate, int loanPaymentCnt, LocalDate startDate,
		double additionalPayment, double escrow, int adjustLockTime, int adjustLoan, double adjustLoanRate ) {
		super();
		
		LoanAmount = loanAmount;
		LoanPaymentCnt = loanPaymentCnt * 12;
		StartDate = startDate;
		AdditionalPayment = additionalPayment;
		bCompoundingOption = false;
		LoanBalanceEnd = 0;
		this.Escrow = escrow;
		
		double  tempRate = interestRate;
		double tempLock = adjustLockTime;

		for(int i = 1; i<=loanPaymentCnt*12; i++) {
            if((adjustLoan != 0) && (adjustLoanRate != 0)) {
                if(i > (tempLock*12)) {
                	tempRate += adjustLoanRate;
                	tempLock += adjustLoan;
                }
            }
            hmRates.put(i, tempRate);
        }
		
		double RemainingBalance = LoanAmount;
		int PaymentCnt = 1;
		

		while(RemainingBalance >= this.GetPMT() + this.AdditionalPayment) {
			InterestRate = this.getInterestRate(PaymentCnt);
			Payment payment = new Payment(RemainingBalance, PaymentCnt++,startDate, this, false);
			RemainingBalance = payment.getEndingBalance();
			startDate = startDate.plusMonths(1);
			loanPayments.add(payment);
		}
		
		
		Payment payment = new Payment(RemainingBalance, PaymentCnt++,startDate, this, true);
		loanPayments.add(payment);

	}

	public double GetPMT() {
		double PMT = 0;
		PMT = FinanceLib.pmt(this.InterestRate/12,
				this.LoanPaymentCnt,
				this.LoanAmount,
				this.LoanBalanceEnd,
				this.bCompoundingOption);
		return Math.abs(PMT);
	}

	public double getTotalPayments() {
		double tot = 0;
		for (Payment val:this.loanPayments) {
			tot += val.getPayment();
		}
		return tot;
	}

	public double getTotalInterest() {

		double interest = 0;
		for (Payment val:this.loanPayments) {
			interest += val.getInterestPayment();
		}
		return interest;

	}

	public double getTotalEscrow() {

		double escrow = 0;
		for (Payment val:this.loanPayments) {
			escrow += val.getEscrowPayment();
		}
		return escrow;

	}

	public double getLoanAmount() {
		return LoanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		LoanAmount = loanAmount;
	}

	public double getLoanBalanceEnd() {
		return LoanBalanceEnd;
	}

	public void setLoanBalanceEnd(double loanBalanceEnd) {
		LoanBalanceEnd = loanBalanceEnd;
	}

	public double getInterestRate(int PaymentCnt) {
		return hmRates.get(PaymentCnt);
	}

	public void setInterestRate(double interestRate) {
		InterestRate = interestRate;
	}

	public int getLoanPaymentCnt() {
		return LoanPaymentCnt;
	}

	public void setLoanPaymentCnt(int loanPaymentCnt) {
		LoanPaymentCnt = loanPaymentCnt;
	}

	public boolean isbCompoundingOption() {
		return bCompoundingOption;
	}

	public void setbCompoundingOption(boolean bCompoundingOption) {
		this.bCompoundingOption = bCompoundingOption;
	}

	public LocalDate getStartDate() {
		return StartDate;
	}

	public void setStartDate(LocalDate startDate) {
		StartDate = startDate;
	}

	public double getAdditionalPayment() {
		return AdditionalPayment;
	}

	public void setAdditionalPayment(double additionalPayment) {
		AdditionalPayment = additionalPayment;
	}

	public ArrayList<Payment> getLoanPayments() {
		return loanPayments;
	}

	public void setLoanPayments(ArrayList<Payment> loanPayments) {
		this.loanPayments = loanPayments;
	}

	public double getEscrow() {
		return Escrow;
	}

}