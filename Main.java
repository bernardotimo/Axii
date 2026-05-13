import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Notifications notifications = new Notifications(true, true, false);
        Settings settings = new Settings("pt-BR", true);
        Identity identity = new Identity("1", "61999999999","123.456.789-10");

        User user = new User(
                "1",
                "Lisa",
                "lisa123@gmail.com",
                "123456",
                LocalDateTime.now(),
                LocalDateTime.now(),
                notifications,
                settings,
                identity
        );

        Bank bank1 = new Bank("1", "Inter", true, "12345-6", 1234);
        Bank bank2 = new Bank("2", "Santander", true, "65432-1", 4321);

        PixKey pix1 = new PixKey("1", "61999999999", "celular");
        PixKey pix2 = new PixKey("2", "lisa123@gmail.com", "e-mail");

        CryptoAsset crypto1 = new CryptoAsset(
                "1",
                "BTC",
                0.25,
                LocalDateTime.now(),
                "Binance",
                "Crypto"
        );

        CryptoAsset crypto2 = new CryptoAsset(
                "2",
                "ETH",
                0.25,
                LocalDateTime.now(),
                "Coinbase",
                "Crypto"
        );

        user.addBank(bank1);
        user.addBank(bank2);
        user.addCryptoAsset(crypto1);
        user.addCryptoAsset(crypto2);
        user.addPixKey(pix1);
        user.addPixKey(pix2);

        user.signUp();
        user.signIn();

        System.out.println("Usuário: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Bancos: " + user.getBanks().size());
        System.out.println("Chaves Pix Cadastradas: " + user.getPixKeys().size());
        System.out.println("Ativos: " + user.getCryptoAssets().size());

        user.signOut();
    }
}