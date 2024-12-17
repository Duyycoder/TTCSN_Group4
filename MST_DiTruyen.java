package MST_GA;

import java.util.*;
// tạo lớp cạnh 
class Canh {
    int nguon;// điểm đầu của cạnh
    int dich;//điểm cuối của cạnh
    int trongSo;// trọng số
    // Constructor để khởi tạo cạnh với nguồn, đích và trọng số
    public Canh(int nguon, int dich, int trongSo) {
        this.nguon = nguon;
        this.dich = dich;
        this.trongSo = trongSo;
    }
    // Override phương thức toString để in thông tin cạnh
    @Override
    public String toString() {
        return "(" + nguon + " - " + dich + ", " + trongSo + ")";
    }
}

class GeneticAlgorithm {
    private List<Canh> danhSachCanh;
    private int soLuongDinh;
    private int soLuongTheHe;
    private int kichThuocQuanThe;
    private Random rand = new Random();// Đối tượng Random để tạo các giá trị ngẫu nhiên
 // Constructor khởi tạo đối tượng GeneticAlgorithm với các tham số
    public GeneticAlgorithm(List<Canh> danhSachCanh, int soLuongDinh, int soLuongTheHe, int kichThuocQuanThe) {
        this.danhSachCanh = danhSachCanh;
        this.soLuongDinh = soLuongDinh;
        this.soLuongTheHe = soLuongTheHe;
        this.kichThuocQuanThe = kichThuocQuanThe;
    }

    // Tạo quần thể ban đầu với kích thước là kíchThuocQuanThe
    private List<List<Canh>> taoQuanThe() {
        List<List<Canh>> quanThe = new ArrayList<>();
        for (int i = 0; i < kichThuocQuanThe; i++) {
            List<Canh> caThe = taoCaThe(); // Tạo một cá thể ngẫu nhiên
            quanThe.add(caThe); // Thêm cá thể vào quần thể
        }
        return quanThe; // Trả về quần thể
    }

    // Tạo một cá thể ngẫu nhiên(tạo một cây khung với số cạnh = số đỉnh - 1)
    private List<Canh> taoCaThe() {
        List<Canh> caThe = new ArrayList<>();
        List<Canh> danhSachCanhCopy = new ArrayList<>(danhSachCanh);// Sao chép danh sách các cạnh
        while (caThe.size() < soLuongDinh - 1) {// Đảm bảo cây khung có số cạnh = số đỉnh - 1
            Canh canh = danhSachCanhCopy.remove(rand.nextInt(danhSachCanhCopy.size())); // Chọn ngẫu nhiên một cạnh
            caThe.add(canh);
        }
        return caThe;
    }

    // Tính trọng số của một cá thể
    int tinhTrongSo(List<Canh> caThe) {
        int tongTrongSo = 0;
        for (Canh canh : caThe) {
            tongTrongSo += canh.trongSo;
        }
        return tongTrongSo;
    }

    // Lai ghép giữa hai cá thể
    private List<Canh> laiGhep(List<Canh> cha, List<Canh> me) {
        List<Canh> con = new ArrayList<>();
        int chiaDiem = rand.nextInt(cha.size());
        for (int i = 0; i < cha.size(); i++) {
            if (i < chiaDiem) {
                con.add(cha.get(i));
            } else {
                con.add(me.get(i));
            }
        }
        return con;
    }

    // Đột biến
    private void dotBien(List<Canh> caThe) {
        int viTriDotBien = rand.nextInt(caThe.size());
        Canh canhMoi = danhSachCanh.get(rand.nextInt(danhSachCanh.size()));
        caThe.set(viTriDotBien, canhMoi);
    }

    // Chọn lọc quần thể
    private List<List<Canh>> chonLoc(List<List<Canh>> quanThe) {
        quanThe.sort(Comparator.comparingInt(this::tinhTrongSo));//Sắp xếp quần thể theo trọng số của các cá thể
        if (quanThe.size() > kichThuocQuanThe) {
            return quanThe.subList(0, kichThuocQuanThe); // Chỉ giữ lại các cá thể tốt nhất
        }
        return quanThe;
    }

    // Giải thuật di truyền
    public List<Canh> giaiThuatDiTruyen() {
        List<List<Canh>> quanThe = taoQuanThe();
        for (int theHe = 0; theHe < soLuongTheHe; theHe++) {
            System.out.println("Thế hệ " + theHe + ": Trọng số MST tốt nhất = " + tinhTrongSo(quanThe.get(0)));

            List<List<Canh>> quanTheMoi = new ArrayList<>();
            for (int i = 0; i < kichThuocQuanThe / 2; i++) {
                List<Canh> cha = quanThe.get(rand.nextInt(quanThe.size()));
                List<Canh> me = quanThe.get(rand.nextInt(quanThe.size()));
                List<Canh> con = laiGhep(cha, me);
                if (rand.nextDouble() < 0.1) {
                    dotBien(con);
                }
                quanTheMoi.add(con);
            }

            quanThe = chonLoc(quanTheMoi);
        }

        return quanThe.get(0);
    }
}

public class MST_DiTruyen {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập số đỉnh: ");
        int soLuongDinh = sc.nextInt();
        System.out.print("Nhập số cạnh: ");
        int soLuongCanh = sc.nextInt();
        List<Canh> danhSachCanh = new ArrayList<>();

        System.out.println("Nhập các cạnh (nguồn, đích, trọng số): ");
        for (int i = 0; i < soLuongCanh; i++) {
            int nguon = sc.nextInt();
            int dich = sc.nextInt();
            int trongSo = sc.nextInt();
            danhSachCanh.add(new Canh(nguon, dich, trongSo));
        }

        System.out.print("Nhập số thế hệ: ");
        int soLuongTheHe = sc.nextInt();

        System.out.print("Nhập kích thước quần thể: ");
        int kichThuocQuanThe = sc.nextInt();

        GeneticAlgorithm ga = new GeneticAlgorithm(danhSachCanh, soLuongDinh, soLuongTheHe, kichThuocQuanThe);
        List<Canh> mst = ga.giaiThuatDiTruyen();

        System.out.println("Cây khung nhỏ nhất cuối cùng: " + mst);
        System.out.println("Trọng số của MST: " + ga.tinhTrongSo(mst));
    }
}
