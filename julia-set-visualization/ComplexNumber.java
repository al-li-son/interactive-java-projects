public class ComplexNumber
{
    private double real;
    private double img;
    public ComplexNumber(double real, double img)
    {
        this.real = real;
        this.img = img;
    }
    public double getReal()
    {
        return real;
    }
    public double getImaginary()
    {
        return img;
    }
    public void setReal(double a)
    {
        real = a;
    }
    public void setImaginary(double a)
    {
        img = a;
    }
    public void setValue(ComplexNumber c)
    {
        real = c.getReal();
        img = c.getImaginary();
    }
    public ComplexNumber add(ComplexNumber c)
    {
        return new ComplexNumber(real + c.getReal(), img + c.getImaginary());
    }
    public ComplexNumber subtract(ComplexNumber c)
    {
        return new ComplexNumber(real - c.getReal(), img - c.getImaginary());
    }
    public ComplexNumber multiply(ComplexNumber c)
    {
        return new ComplexNumber(real*c.getReal() - img*c.getImaginary(), real*c.getImaginary() + img*c.getReal());
    }
    public ComplexNumber divide(ComplexNumber c)
    {
        double a = c.getReal();
        double b = c.getImaginary();
        return new ComplexNumber((real*a + img*b)/(a*a + b*b), (img*a - real*b)/(a*a + b*b));
    }
}