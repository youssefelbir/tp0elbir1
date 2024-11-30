package ma.emsi.elbir1.tp0elbir1.Bb;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Backing bean pour la page JSF index.xhtml.
 * Portée view pour conserver l'état de la conversation pendant plusieurs requêtes HTTP.
 */
@Named
@ViewScoped
public class Bb implements Serializable {

    private String systemRole;
    private boolean systemRoleChangeable = true;
    private String question;
    private String reponse;
    private StringBuilder conversation = new StringBuilder();

    @Inject
    private FacesContext facesContext;

    public Bb() {
    }

    public String getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

    public boolean isSystemRoleChangeable() {
        return systemRoleChangeable;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getConversation() {
        return conversation.toString();
    }

    public void setConversation(String conversation) {
        this.conversation = new StringBuilder(conversation);
    }

    /**
     * Génère une citation inspirante aléatoire à renvoyer.
     */
    private String obtenirCitationAleatoire() {
        List<String> citations = new ArrayList<>();
        citations.add("« La vie est ce qui se passe quand vous êtes occupé à faire d'autres projets. » – John Lennon");
        citations.add("« Le succès c'est d'aller d'échec en échec sans perdre son enthousiasme. » – Winston Churchill");
        citations.add("« Ne jamais se retourner, regarder toujours devant soi. » – Albert Einstein");
        citations.add("« Le seul moyen de faire du bon travail est d’aimer ce que vous faites. » – Steve Jobs");
        citations.add("« Faites aujourd’hui ce que les autres ne veulent pas faire, demain vous ferez ce que les autres ne peuvent pas faire. » – Jerry Rice");

        Random rand = new Random();
        return citations.get(rand.nextInt(citations.size()));
    }

    /**
     * Envoie la question au serveur.
     * En attendant de l'envoyer à un LLM, le serveur fait un traitement quelconque.
     * Le traitement consiste à répondre avec une citation inspirante.
     *
     * @return null pour rester sur la même page.
     */
    public String envoyer() {
        if (question == null || question.isBlank()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Texte question vide", "Il manque le texte de la question");
            facesContext.addMessage(null, message);
            return null;
        }

        // Utiliser une citation inspirante à la place de manipuler la question.
        this.reponse = "|| " + obtenirCitationAleatoire() + " ||";

        // Si la conversation n'a pas encore commencé, ajouter le rôle système au début de la réponse.
        if (this.conversation.isEmpty()) {
            this.reponse = systemRole.toUpperCase(Locale.FRENCH) + "\n" + this.reponse;
            this.systemRoleChangeable = false;
        }

        afficherConversation();
        return null;
    }

    public String nouveauChat() {
        return "index";
    }

    private void afficherConversation() {
        this.conversation.append("== User:\n").append(question).append("\n== Serveur:\n").append(reponse).append("\n");
    }

    public List<SelectItem> getSystemRoles() {
        List<SelectItem> listeSystemRoles = new ArrayList<>();
        String role = """
                You are a helpful assistant. You help the user to find the information they need.
                If the user type a question, you answer it.
                """;
        listeSystemRoles.add(new SelectItem(role, "Assistant"));
        role = """
                You are an interpreter. You translate from English to French and from French to English.
                If the user type a French text, you translate it into English.
                If the user type an English text, you translate it into French.
                If the text contains only one to three words, give some examples of usage of these words in English.
                """;
        listeSystemRoles.add(new SelectItem(role, "Traducteur Anglais-Français"));
        role = """
                Your are a travel guide. If the user type the name of a country or of a town,
                you tell them what are the main places to visit in the country or the town
                and you tell them the average price of a meal.
                """;
        listeSystemRoles.add(new SelectItem(role, "Guide touristique"));
        this.systemRole = (String) listeSystemRoles.getFirst().getValue();
        return listeSystemRoles;
    }
}
