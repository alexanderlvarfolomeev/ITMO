package ru.itmo.tpl.util;

import ru.itmo.tpl.model.Post;
import ru.itmo.tpl.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mikhail Mirzayanov", User.Colors.GREEN),
            new User(2, "tourist", "Genady Korotkevich", User.Colors.RED),
            new User(3, "emusk", "Elon Musk", User.Colors.GREEN),
            new User(5, "pashka", "Pavel Mavrin", User.Colors.RED),
            new User(7, "geranazavr555", "Georgiy Nazarov", User.Colors.BLUE),
            new User(11, "cannor147", "Erofey Bashunov", User.Colors.BLUE)
    );

    private static final List<Post> POSTS = Arrays.asList(
            new Post(1, "Технокубок 2020 — олимпиада по программированию для школьников",
                    "Mail.Ru Group совместно с МФТИ, МГТУ им. Н. Э. Баумана и Codeforces" +
                            " в пятый раз запускает олимпиаду по программированию для школьников — «Технокубок»." +
                            " В этом учебном году олимпиада вошла в перечень олимпиад школьников, повысив свой" +
                            " уровень до самого высокого — первого: теперь победители и призеры могут быть зачислены" +
                            " на профильные факультеты российских вузов без вступительных экзаменов. Кроме того" +
                            " лучшие участники получат ценные призы от компании Apple." +
                            "Сразиться за звание самого талантливого молодого программиста приглашаются учащиеся" +
                            " средних образовательных учреждений 8-11 классов." +
                            "Победители и призеры олимпиады будут определены по результатам очного этапа," +
                            " который пройдет 1 марта 2020 года на базе МФТИ, МГТУ им. Н.Э.Баумана, а также" +
                            " на других региональных площадках по всей России, о которых будет сообщено позднее.", 1),
            new Post(4, "О сорванном раунде 591/Технокубок 2020 — Отборочный Раунд 1",
                    "Привет, Codeforces!" +
                            "К сожалению, недоброжелатели сорвали проведение раунда, сделав DDOS на нашу инфраструктуру." +
                            " Ни координатор, ни авторы раунда не виноваты, что у вас не получилось полноценно принять участие в раунде." +
                            " Пожалуйста, не минусуйте анонс раунда. Я думаю, что такая ситуация — дополнительный повод поддержать авторов." +
                            " Они подготовили хорошие задачи!" +
                            "Видимо, подобную атаку надо расценивать как симптом того," +
                            " что Codeforces перерос фазу юношества и вступил во взрослую серьезную жизнь." +
                            " Конечно, мы ответим адекватными мерами, чтобы защититься от подобных инцидентов." +
                            " К счастью, за почти 10 лет работы вокруг сложилось большое сообщество тех, кому небезразличен Codeforces." +
                            " Мы не переживаем по поводу возможных дополнительных трат или приложенных усилий. У нас всё получится." +
                            " Раунды должны продолжаться.Привет, Codeforces!", 1),
            new Post(5, "Codeforces Global Round 5",
                    "Всем привет! " +
                            "Скоро состоится Codeforces Global Round 5, время начала: среда, 16 октября 2019 г. в 17:35. " +
                            "Это пятый раунд из серии Codeforces Global Rounds, которая проводится при поддержке XTX Markets." +
                            " В раундах могут участвовать все, рейтинг тоже будет пересчитан для всех. " +
                            "Количество задач, длительность и разбалловка будут объявлены позднее. " +
                            "Призы в этом раунде: 30 лучших участников получат футболки. " +
                            "20 футболок будут разыграны случайным образом среди участников с 31-го по 500-е место. " +
                            "Призы в серии из 6 раундов в 2019 году: " +
                            "За каждый раунд лучшим 100 участникам начисляются баллы согласно таблице. " +
                            "Итоговый результат участника равны сумме баллов для четырех лучших выступлений этого участника. " +
                            "Лучшие 20 участников по итоговым результатам получают толстовки и сертификаты с указанием места." +
                            "Задачи раунда разработаны мной, а тех, без кого этот раунд бы не состоялся, я также поблагодарю чуть позже. " +
                            "Раунд будет идеально сбалансирован. Присоединяйтесь!", 2)
    );

    private static final List<Map.Entry<String, String>> MENU_ITEMS = Arrays.asList(
            Map.entry("/index", "Index"),
            Map.entry("/misc/help", "Help"),
            Map.entry("/users", "Users")
    );

    private static List<User> getUsers() {
        return USERS;
    }

    private static List<Post> getPosts() {
        List<Post> posts = POSTS;
        posts.sort((Post firstPost, Post secondPost)
                -> Long.compare(secondPost.getId(), firstPost.getId()));
        return posts;
    }

    private static List<Map.Entry<String, String>> getMenuItems() {
        return MENU_ITEMS;
    }

    public static void putData(Map<String, Object> data) {
        data.put("users", getUsers());
        data.put("posts", getPosts());
        data.put("menuItems", getMenuItems());

        for (User user : getUsers()) {
            if (data.get("logged_user_id") != null && user.getId() == (Long) data.get("logged_user_id")) {
                data.put("user", user);
            }
        }
    }
}
