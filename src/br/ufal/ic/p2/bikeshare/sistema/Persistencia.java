package br.ufal.ic.p2.bikeshare.sistema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;

/**
 * Salva e carrega o {@link Sistema} inteiro com {@code java.io.Serializable} (PDF §10).
 * Como o grafo de objetos é gravado de uma vez, usuários, estações, bicicletas, aluguéis,
 * planos e contadores de id voltam exatamente como estavam.
 */
public final class Persistencia {
    private static final File ARQUIVO = new File("bikeshare_data.ser");

    private Persistencia() {
    }

    /** Carrega o estado salvo; se não houver arquivo, começa um sistema vazio. */
    public static Sistema carregar() {
        if (!ARQUIVO.exists()) {
            return new Sistema();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            return (Sistema) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // arquivo de uma versão antiga ou corrompido: começa do zero em vez de quebrar
            return new Sistema();
        }
    }

    /** Grava o estado atual em disco. */
    public static void salvar(Sistema sistema) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            out.writeObject(sistema);
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao salvar o sistema.", e);
        }
    }

    /** Apaga o arquivo salvo (usado ao zerar o sistema). */
    public static void apagar() {
        ARQUIVO.delete();
    }
}
