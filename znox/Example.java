package znox;

import org.bukkit.entity.Player;

public class Example extends Fake {

    // Metodo de exemplo
    public void example(Player player) {
        Integer integer = null;
        setPlayer(player);

        if (integer == 0) {

            // Aplicar o Fake
            setName(getPlayer().getName().substring(0, 1));

            setFake();
        } else {

            // Remover o fake
            setName(getPlayer().getName());

            setFake();
        }
    }

}
