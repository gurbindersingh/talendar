public class main {
    public static void main(String[] args) {
        System.out.println("Start\n ");
        int x = 4;
        int y = 25;
        boolean found = false;
        int count = 18;
        while(!found){
            System.out.println(".\n");
            if((x+count) % 21 == 0 && (y + count) % 21 ==0){

                System.out.println("The numbers are " + (x+count) + ", " + (y+count) + " iterated by: " + count);
                found = true;
            }
            count++;
        }
    }
}
