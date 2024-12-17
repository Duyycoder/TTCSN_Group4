package MST_boruvka;
import java.util.*;

// Lớp đại diện cho một cạnh trong đồ thị
class Canh {
    int nguon, dich, trongSo;

    // Hàm khởi tạo cạnh
    Canh(int nguon, int dich, int trongSo) {
        this.nguon = nguon;
        this.dich = dich;
        this.trongSo = trongSo;
    }
}

// Lớp thực thi thuật toán Boruvka để tìm cây khung nhỏ nhất
public class BoruvkaMST {
    private int soKhuVuc; // Số đỉnh (khu vực) trong đồ thị
    private List<Canh> cacCanh; // Danh sách các cạnh trong đồ thị

    // Hàm khởi tạo đồ thị
    public BoruvkaMST(int soKhuVuc) {
        this.soKhuVuc = soKhuVuc;
        this.cacCanh = new ArrayList<>();
    }

    // Phương thức thêm một cạnh vào đồ thị
    public void themCanh(int nguon, int dich, int trongSo) {
        cacCanh.add(new Canh(nguon, dich, trongSo));
    }

    // Tìm đại diện của tập hợp chứa đỉnh i (Union-Find)
    public int find(int[] cha, int i) {
        if (cha[i] != i) {
            cha[i] = find(cha, cha[i]); // Gán trực tiếp cha của i để tối ưu hóa
        }
        return cha[i];
    }

    // Hợp nhất hai tập hợp chứa đỉnh x và y (Union-Find)
    public void union(int[] cha, int[] capBac, int x, int y) {
        int gocX = find(cha, x);
        int gocY = find(cha, y);

        // Hợp nhất dựa trên cấp bậc để tối ưu
        if (capBac[gocX] < capBac[gocY]) {
            cha[gocX] = gocY;
        } else if (capBac[gocX] > capBac[gocY]) {
            cha[gocY] = gocX;
        } else {
            cha[gocY] = gocX;
            capBac[gocX]++;
        }
    }

    // Thực hiện thuật toán Boruvka để tìm cây khung nhỏ nhất
    public void boruvkaMST() {
        int[] cha = new int[soKhuVuc]; // Mảng đại diện cho tập hợp
        int[] capBac = new int[soKhuVuc]; // Mảng lưu cấp bậc của cây
        Canh[] reNhoNhat = new Canh[soKhuVuc]; // Mảng lưu cạnh nhỏ nhất của mỗi tập hợp
        int soCay = soKhuVuc; // Ban đầu, mỗi đỉnh là một cây riêng
        int tongTrongSo = 0; // Tổng trọng số của cây khung nhỏ nhất

        // Khởi tạo các tập hợp và cấp bậc ban đầu
        for (int i = 0; i < soKhuVuc; i++) {
            cha[i] = i;
            capBac[i] = 0;
        }

        // Vòng lặp chính của thuật toán
        while (soCay > 1) {
            Arrays.fill(reNhoNhat, null); // Reset mảng cạnh nhỏ nhất

            // Tìm cạnh nhỏ nhất của mỗi tập hợp
            for (Canh canh : cacCanh) {
                int tapHop1 = find(cha, canh.nguon);
                int tapHop2 = find(cha, canh.dich);

                // Nếu hai đỉnh thuộc hai tập hợp khác nhau
                if (tapHop1 != tapHop2) {
                    if (reNhoNhat[tapHop1] == null || canh.trongSo < reNhoNhat[tapHop1].trongSo) {
                        reNhoNhat[tapHop1] = canh;
                    }
                    if (reNhoNhat[tapHop2] == null || canh.trongSo < reNhoNhat[tapHop2].trongSo) {
                        reNhoNhat[tapHop2] = canh;
                    }
                }
            }

            // Hợp nhất các tập hợp dựa trên các cạnh nhỏ nhất tìm được
            for (int i = 0; i < soKhuVuc; i++) {
                if (reNhoNhat[i] != null) {
                    Canh canh = reNhoNhat[i];
                    int tapHop1 = find(cha, canh.nguon);
                    int tapHop2 = find(cha, canh.dich);

                    // Nếu hai đỉnh vẫn thuộc hai tập hợp khác nhau
                    if (tapHop1 != tapHop2) {
                        tongTrongSo += canh.trongSo; // Cộng trọng số cạnh vào tổng chi phí
                        union(cha, capBac, tapHop1, tapHop2); // Hợp nhất tập hợp
                        System.out.println("Cạnh được thêm: (" + canh.nguon + ", " + canh.dich + ") với trọng số " + canh.trongSo);
                        soCay--; // Giảm số lượng tập hợp
                    }
                }
            }
        }

        // Kết quả cuối cùng
        System.out.println("Tổng trọng số của cây khung nhỏ nhất: " + tongTrongSo);
    }

    // Hàm chính để chạy chương trình
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Nhập số khu vực (đỉnh): ");
            int soKhuVuc = scanner.nextInt();

            System.out.print("Nhập số kết nối khả thi (cạnh): ");
            int soCanh = scanner.nextInt();

            BoruvkaMST doThi = new BoruvkaMST(soKhuVuc);

            System.out.println("Nhập các cạnh (nguồn, đích, trọng số):");
            for (int i = 0; i < soCanh; i++) {
                int nguon = scanner.nextInt();
                int dich = scanner.nextInt();
                int trongSo = scanner.nextInt();
                doThi.themCanh(nguon, dich, trongSo);
            }

            // Thực hiện thuật toán Boruvka
            doThi.boruvkaMST();

        } catch (InputMismatchException e) {
            System.err.println("Lỗi: Vui lòng nhập đúng định dạng số nguyên.");
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
