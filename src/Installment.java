import java.util.Date;


/**
 *
 * @author Luca and Friends :: github.com/lfriends
 */
public class Installment {
    
    private final int number;
    private double total;
    private double principal;
    private double interests;
    private double outstandingPrincipal ;
    private double outstandingInterests; 
    private double debtPaid; 
    private final java.util.Date dueDate;

    public Installment(Plan plan, int currentInstallmentNumber ) {
        
        this.number = currentInstallmentNumber;
        this.total = plan.getSingleInstallmentAmount();
        boolean firstIntallment = currentInstallmentNumber==1 ;
        boolean lastInstallment = plan!=null && currentInstallmentNumber==plan.getNumberOfInstallments() ;
        
        Installment prevInstallment = null ;
        if (number>1)prevInstallment=plan.getInstallments().get(currentInstallmentNumber-2);
        
        if (firstIntallment){
            principal = Utils.myRound( plan.getSingleInstallmentAmount() - (plan.getPrincipalAmount()-plan.getAdvancePaymentAmount() )* plan.getInterestRatePerMonth()  , plan.NUMBER_OF_DECIMALS );
        }else{
            principal = Utils.myRound(prevInstallment .principal * (1 + plan.getInterestRatePerMonth() ) , plan.NUMBER_OF_DECIMALS );
        }
        
        if (plan.getInterestRatePerMonth()>0){
            interests = Utils.myRound( plan.getSingleInstallmentAmount() - principal , plan.NUMBER_OF_DECIMALS );
        }else{
            interests = 0 ;
            total = principal;
        }
        
        // last installment round correction 
        if (lastInstallment){
            principal = Utils.myRound( prevInstallment.outstandingPrincipal , plan.NUMBER_OF_DECIMALS );
            interests = prevInstallment.outstandingInterests ;
        }
            
        total = principal + interests ;
                
        if (currentInstallmentNumber==1){
            dueDate = plan.getFirstDueDate();
        }else{
            int day = Utils.getDayOfMonth( plan.getFirstDueDate() );
            java.util.Date newDate = Utils.getFirstDayOfMonth( plan.getFirstDueDate() );
            newDate = Utils.addMonth( newDate, currentInstallmentNumber-1 );
            int month = Utils.getMonthNumber( newDate );
            newDate = Utils.addDay( newDate, day-1 );
            
            while ( month <  Utils.getMonthNumber( newDate ) )
                newDate = Utils.addDay( newDate, -1 );
                    
            dueDate =  newDate;
        }
        
        updateRemainingAmounts(plan);
    }

    public int getNumber() {
        return number;
    }

    public double getTotalAmount() {
        return total;
    }

    public double getPrincipalAmount() {
        return principal;
    }

    public double getInterestAmount() {
        return interests;
    }

    public double getOutstandingPrincipal() {
        return outstandingPrincipal;
    }

    public double getOutstandingInterests() {
        return outstandingInterests;
    }

    public double getDebtPaid() {
        return debtPaid;
    }

    public Date getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return 
            "\nInstallment{#=" + number 
             + ", dueDate=" + Utils.date2s(dueDate) 
             + ", total=" + Utils.double2s(total) 
             + ", principal=" + Utils.double2s(principal) 
             + ", interests=" + Utils.double2s(interests) 
             + ", debt=" + Utils.double2s(outstandingPrincipal) 
             + ", interestsOuts=" + Utils.double2s(outstandingInterests) 
             + '}';
    }

    protected void setInterestAmount(double d) {
        this.interests = d ;
    }

    protected void setTotalAmount(double d) {
        this.total = d ;
    }

    protected void setPrincipalAmount(double d) {
        this.principal = d ;
    }

    public double getOutstandingTotal() {
        return getOutstandingPrincipal() + getOutstandingInterests() ;
    }

    void updateRemainingAmounts(Plan plan) {
        
        /* updating
        debtPaid (principal)
        outstantingPrincipal
        outstandingInterest
        */
        
        Installment prevInstallment = null ;
        if (number>1)prevInstallment=plan.getInstallments().get(number-2);
        boolean firstIntallment = number==1 ;
        boolean lastInstallment = plan!=null && number==plan.getNumberOfInstallments() ;
        
        if (firstIntallment){
            debtPaid =  Utils.myRound( principal , plan.NUMBER_OF_DECIMALS ) ;
            outstandingPrincipal = Utils.myRound( plan.getPrincipalAmount()-principal - plan.getAdvancePaymentAmount() , plan.NUMBER_OF_DECIMALS ) ;
            outstandingInterests =  Utils.myRound( plan.getInterestAmount()  - interests , plan.NUMBER_OF_DECIMALS ) ;
        }else{
            debtPaid = Utils.myRound( prevInstallment.debtPaid + principal, plan.NUMBER_OF_DECIMALS );
            outstandingPrincipal = Utils.myRound( prevInstallment.outstandingPrincipal - principal , plan.NUMBER_OF_DECIMALS );
            outstandingInterests =  Utils.myRound( prevInstallment.outstandingInterests  - interests, plan.NUMBER_OF_DECIMALS );
        }
        
        if (lastInstallment){
            outstandingPrincipal = 0 ;
            outstandingInterests = 0 ;
        }
        

    }
    
}
