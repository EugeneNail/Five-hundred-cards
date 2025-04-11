import java.util.*;
import java.util.concurrent.*;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;


public class CardGameServer {
    private static final int WHITE_CARDS_COUNT = 500;
    private static final int RED_CARDS_COUNT = 100;
    private static final int CARDS_PER_PLAYER = 10;

    private static Map<String, Player> players = new ConcurrentHashMap<>();
    private static List<String> whiteCards = new ArrayList<>();
    private static List<String> redCards = new ArrayList<>();
    private static GameState gameState = new GameState();

    public static void main(String[] args) throws Exception {
        whiteCards.add("Танцор, которому не мешают яйцы");
        whiteCards.add("Дедушка Мороз");
        whiteCards.add("Китайские собаки в колготах");
        whiteCards.add("Владыка Распердыка");
        whiteCards.add("Бизнес-тренинг в борделе");
        whiteCards.add("Порнофильм, оканчивающийся свадьбой");
        whiteCards.add("Животная оргия в доме престарелых");
        whiteCards.add("Мех вуглускра");
        whiteCards.add("Сосед в костюме Деда Мороза");
        whiteCards.add("Нейрохирургия шилом");

        whiteCards.add("Клизма с отваром ромашки");
        whiteCards.add("Лечебные грибы, помогающие бороться с волнением перед экзаменом");
        whiteCards.add("Похотливая сушка");
        whiteCards.add("Полный лоток перцового мороженого");
        whiteCards.add("Подкуп гаишника печеньками");
        whiteCards.add("Гордый еж-летун");
        whiteCards.add("Летняя рыбалка на упорыша");
        whiteCards.add("Шпион в женской раздевалке");
        whiteCards.add("Тверкинг");
        whiteCards.add("Рыцарь в латексных доспехах");

        whiteCards.add("Юный сатанист-филантроп");
        whiteCards.add("Водяной пистолет с текилой");
        whiteCards.add("Сайт знакомств для животных");
        whiteCards.add("Легкий флирт в мужском туалете");
        whiteCards.add("Котаин");
        whiteCards.add("Облегчение в штаны");
        whiteCards.add("Корабль радости");
        whiteCards.add("Вынужденные извращения");
        whiteCards.add("Наглое уклонение от уплаты налогов");
        whiteCards.add("Петтинг");

        whiteCards.add("Расплавленный сыр");
        whiteCards.add("Клизма из настоя утренней росы");
        whiteCards.add("Поход во внутренний мир");
        whiteCards.add("Финалист шоу 'Волос'");
        whiteCards.add("Дураки и дороги");
        whiteCards.add("Сосед сверху, который топает, как слон");
        whiteCards.add("Зажигательные речи Виталия Кличко");
        whiteCards.add("Амфетамины");
        whiteCards.add("Коренной московский таджик");
        whiteCards.add("Сосед по даче");

        whiteCards.add("Теплое пиво");
        whiteCards.add("Мои большие яйца");
        whiteCards.add("Ограбление Ксении Собчак толпой пенсионеров");
        whiteCards.add("Слабоумие и отвага");
        whiteCards.add("Пиджак, изящно заправленный в брюки");
        whiteCards.add("Кошмар на проспекте Ленина");
        whiteCards.add("Секс, ложь и борщ");
        whiteCards.add("Внедорожная версия секса");
        whiteCards.add("Неприкрытые гениталии");
        whiteCards.add("Запах секса");

        whiteCards.add("Гавнодушие");
        whiteCards.add("Извращенные фантазии");
        whiteCards.add("Человек-сосед");
        whiteCards.add("Конь Михаила Боярского в качестве депутата Госдумы");
        whiteCards.add("Бешеная свиноматка");
        whiteCards.add("Восковая фигура Путина");
        whiteCards.add("Злобный карлик");
        whiteCards.add("Властные органы в руках рабочих");
        whiteCards.add("Горькая попка огурца");
        whiteCards.add("Похотливая гусеница");

        whiteCards.add("Депрессивный трансвестит");
        whiteCards.add("Встреча выпускников");
        whiteCards.add("Курение вагиной");
        whiteCards.add("Нудисты в женском монастыре");
        whiteCards.add("Губы уточкой");
        whiteCards.add("Бормотание из-под шконки");
        whiteCards.add("Скучающий Антихрист");
        whiteCards.add("Ерунда какая-то");
        whiteCards.add("Шепелявый бездомный музыкант");
        whiteCards.add("Миллиард комаров");

        whiteCards.add("Зона отчуждения");
        whiteCards.add("Доклад начальника транспортного цеха");
        whiteCards.add("Хватание за причиндалы");
        whiteCards.add("Симулятор симуляции оргазма");
        whiteCards.add("Пачка пельменей");
        whiteCards.add("Апофигей опупеоза");
        whiteCards.add("Секс-кукла вуду");
        whiteCards.add("Жесткое немецкое порно");
        whiteCards.add("Секс по службе");
        whiteCards.add("Утренний поход до ванной враскоряку");

        whiteCards.add("Продажная тушенка");
        whiteCards.add("Проститутка на пенсии");
        whiteCards.add("Чувственый маразм");
        whiteCards.add("Ассортимент секс-шопа");
        whiteCards.add("Первокурсницы");
        whiteCards.add("Выражение глубокого увлажнения");
        whiteCards.add("Черт знает что");
        whiteCards.add("Селфи");
        whiteCards.add("Бахчисарайский сарай");
        whiteCards.add("Серийный самоубийца");

        whiteCards.add("Николай Валуев, поющий колыбельную");
        whiteCards.add("Стадо бронтозавров");
        whiteCards.add("Теплый ламповый звук в моем смартфоне");
        whiteCards.add("Грациозные ленивцы на унициклах");
        whiteCards.add("Чудовище, живущее под одеялом");
        whiteCards.add("Сало единорога");
        whiteCards.add("Дрыночная экономика");
        whiteCards.add("Суровый трудоголик");
        whiteCards.add("Вьетнамки");
        whiteCards.add("Секс по дружбе");

        whiteCards.add("Ктулху");
        whiteCards.add("Запихивание носка в плавки перед походом на пляж");
        whiteCards.add("Дискотека 'Если вам за 50'");
        whiteCards.add("Скачивание всего интернета на флешку");
        whiteCards.add("Православие головного мозга");
        whiteCards.add("Очередная игра в танчики");
        whiteCards.add("Мощный приход");
        whiteCards.add("Оборотни в погонах");
        whiteCards.add("Бесславная карьера профессионального смотрельщика сериалов");
        whiteCards.add("Дезодорант с запахом чизбургера");

        whiteCards.add("Хрустальная салатница с кровью вьетнамской девственницы");
        whiteCards.add("Плацебо");
        whiteCards.add("Волшебные грибы");
        whiteCards.add("Свобода самовыражения");
        whiteCards.add("Подруга, которая затыкается, только когда спит");
        whiteCards.add("Кошачий корм");
        whiteCards.add("Анальное ущемление");
        whiteCards.add("Галоперидол");
        whiteCards.add("Букет борщевиков");
        whiteCards.add("Гипножаба");

        whiteCards.add("Великая паника");
        whiteCards.add("Передний зуб Овечкина");
        whiteCards.add("4 литра самогона");
        whiteCards.add("Гигантские роботы, убивающие всё");
        whiteCards.add("Дедушкина вставная челюсть");
        whiteCards.add("Приоткрытые врата сладострастия");
        whiteCards.add("Клоун на антидепрессантах");
        whiteCards.add("Галлюциногены");
        whiteCards.add("Ковбойские штаны");
        whiteCards.add("Лабрадор Владимира Путина");

        whiteCards.add("Засорение канализации");
        whiteCards.add("Пердеж в бассейне");
        whiteCards.add("Инопланетная форма жизни");
        whiteCards.add("Смерть от рук Чака Норриса");
        whiteCards.add("Человек -- летучий хомяк");
        whiteCards.add("Сиськи-письки");
        whiteCards.add("Засорение канализации");
        whiteCards.add("Ведро гвоздей");
        whiteCards.add("Симбиоз с ксеноморфом");
        whiteCards.add("Няш-мяш");

        whiteCards.add("Мозги Саши Грей");
        whiteCards.add("Собака мордой вниз");
        whiteCards.add("Мармеладные мишки");
        whiteCards.add("Зомби-некрофил");
        whiteCards.add("Лапша быстрого реагирования");
        whiteCards.add("Всеобщий троллинг");
        whiteCards.add("Расстрелы и репрессии");
        whiteCards.add("Хмельная радость");
        whiteCards.add("Неопровержимые улитки");
        whiteCards.add("Тяжелый самоходный огнемет 'Буратино'");

        whiteCards.add("Удовольствие через боль");
        whiteCards.add("Булава Всевластья");
        whiteCards.add("Богоподобие в спальне");
        whiteCards.add("Какодемон");
        whiteCards.add("Выжигание напалмом");
        whiteCards.add("Лилипуты");
        whiteCards.add("Традиционные семейные ценности");
        whiteCards.add("Штраф за неправильную парковку");
        whiteCards.add("Комочек в пупке");
        whiteCards.add("Дефлорация через депиляцию");

        whiteCards.add("Секс, наркотики, морожение и котики");
        whiteCards.add("Метающий гомострелы Амур");
        whiteCards.add("Бессонница");
        whiteCards.add("Смысл Дня народного единства");
        whiteCards.add("Глубокий горловой сонет");
        whiteCards.add("Окаменевший хобот мамонта");
        whiteCards.add("Цветной струйный сфинктер");
        whiteCards.add("Похмелье от кефира");
        whiteCards.add("Психолог эксгибиционист");
        whiteCards.add("Водитель троллейбуса");

        whiteCards.add("Гоша Куценко в розовом парике");
        whiteCards.add("Отпугиватель кротов");
        whiteCards.add("Что за чушь");
        whiteCards.add("Проказа");
        whiteCards.add("Чума");
        whiteCards.add("Убийство на почве ревности");
        whiteCards.add("Легкость нытия");
        whiteCards.add("Пижамная вечеринка в армейской казарме");
        whiteCards.add("Каймановая черепаха-членокус");
        whiteCards.add("Животноводство");

        whiteCards.add("Исполнение рок-оперы в душе");
        whiteCards.add("Старый альбом с голыми фотографиями");
        whiteCards.add("Монобровь");
        whiteCards.add("Владимир Путин");
        whiteCards.add("Белоснежка и семь гномов в новом порнофильме");
        whiteCards.add("Лосось-соблазнитель");
        whiteCards.add("Тараканы в голове");
        whiteCards.add("Печеньки");
        whiteCards.add("Винное поле");
        whiteCards.add("Искрометный юмор");

        whiteCards.add("Француз-любовник");
        whiteCards.add("Террористы и котики");
        whiteCards.add("Таблетки от глистов");
        whiteCards.add("40 кошек");
        whiteCards.add("Поза наездницы во время оргии на ипподроме");
        whiteCards.add("Яблоки со своей дачи");
        whiteCards.add("Велосипедисты под кайфом");
        whiteCards.add("Плюшка");
        whiteCards.add("Секс в больошм торте");
        whiteCards.add("Розовенькое трико");

        whiteCards.add("Моя прелесть");
        whiteCards.add("Секс без причины");
        whiteCards.add("Ностальгия по ремню отца");
        whiteCards.add("Черный Властелин");
        whiteCards.add("ЭТО СПАРТАААААААА!!!");
        whiteCards.add("Ведро кефира");
        whiteCards.add("Глазированные сырки");
        whiteCards.add("Белка и Стрелка");
        whiteCards.add("Трехэтажный стих");
        whiteCards.add("Непроизвольный секс в автобусе в час пик");

        whiteCards.add("Чудище для сексуальных утех");
        whiteCards.add("Клонированная лошадь");
        whiteCards.add("Хипстеры");
        whiteCards.add("Членовредительство");
        whiteCards.add("Политические споры");
        whiteCards.add("Незапланированная беременность");
        whiteCards.add("Носки, стоящие в углу");
        whiteCards.add("Бразильская эпиляция");
        whiteCards.add("Фригидная женщина");
        whiteCards.add("Поход к шефу на ковер");

        whiteCards.add("Пол-литра");
        whiteCards.add("Витиеватые испражнения");
        whiteCards.add("Мозолистые ручки китайских рабочих");
        whiteCards.add("Котики и сиськи");
        whiteCards.add("Ящерки-пришельцы, управляющие нашим сознанием");
        whiteCards.add("Избалованные дети");
        whiteCards.add("Аромат немытых тел в общественном транспорте");
        whiteCards.add("Пьянство в одно жало");
        whiteCards.add("Секс в туалете ночного клуба");
        whiteCards.add("Полосатый слон");

        whiteCards.add("Жесткое немецкое поражение");
        whiteCards.add("Необъяснимое желание секса с овощами");
        whiteCards.add("Шварцнеггер");
        whiteCards.add("Беспросветная стабильность современной России");
        whiteCards.add("Лоснящиеся щеки Ивана Урганта");
        whiteCards.add("Полуметровые бутерброды");
        whiteCards.add("Обсасывание колпачка у ручки");
        whiteCards.add("Большой шоколадный тлен");
        whiteCards.add("Хор упоротых медведей в горящей машине");
        whiteCards.add("Мтеание дротиков в портрет начальника");

        whiteCards.add("Лженаука");
        whiteCards.add("Бессовестные хулиганы");
        whiteCards.add("Громыхание палкой внутри помойного ведра");
        whiteCards.add("Анальные шарики в качестве шейного массажера");
        whiteCards.add("Камасутра в комиксах");
        whiteCards.add("Раскладывание пасьянса на работе");
        whiteCards.add("Богатый внутренний мир пятого размера");
        whiteCards.add("Страхование от порабощения пришельцами");
        whiteCards.add("Увольнение после корпоратива");
        whiteCards.add("Жопоболь");

        whiteCards.add("Полуфабрикаты");
        whiteCards.add("Церковное пение");
        whiteCards.add("Необъятные штаны Деда Мороза");
        whiteCards.add("Поедание бороды Перельмана, чтобы получит его знания");
        whiteCards.add("Впивающиеся в анус стринги");
        whiteCards.add("Движение в защиту неполноценных политиков");
        whiteCards.add("Окончательное раскрытие темы");
        whiteCards.add("Зомби-апокалипсис");
        whiteCards.add("Пимпочка");
        whiteCards.add("Безрукий клоун");

        whiteCards.add("Клоун без юмора");
        whiteCards.add("Говно мамонта");
        whiteCards.add("Ночная прогулка по спальному району Москвы");
        whiteCards.add("Срань Господня");
        whiteCards.add("Кровавая гебня");
        whiteCards.add("Премия 'Лучший шаурмен России'");
        whiteCards.add("Душевные гадости");
        whiteCards.add("Гинеколог, разворотивший чертово логово");
        whiteCards.add("Блондинки, красящие корни волос в черный цвет");
        whiteCards.add("Ритмы шансона");

        whiteCards.add("Рыбалка в городском фонтане");
        whiteCards.add("Настойчивое желание съесть пачку пельменей");
        whiteCards.add("Непонятные намеки голой женщины в постели");
        whiteCards.add("Надежда Бабкина и ее мужчины");
        whiteCards.add("Бокальные данные");
        whiteCards.add("Случайный секс в полицейском участке");
        whiteCards.add("Волшебный порошок");
        whiteCards.add("Влажная встреча");
        whiteCards.add("Детская неожиданность");
        whiteCards.add("Старшеклассницы");

        whiteCards.add("Секс как способ знакомства");
        whiteCards.add("Угроза нервного срыва");
        whiteCards.add("Кино про извращенцев");
        whiteCards.add("Тупая гильотина");
        whiteCards.add("1.5кг картофельного пюре");
        whiteCards.add("Чилавек-малекула!");
        whiteCards.add("Надувание лягушек");
        whiteCards.add("Доставание из широких штанин");
        whiteCards.add("Бутылка 3 в 1: гель, шампунь, кетчуп");
        whiteCards.add("Хрустящие потягушки");

        whiteCards.add("Вальсирующие полицейские");
        whiteCards.add("Борис Моисеев, поющий во сне Аркадия Укупника");
        whiteCards.add("Надоедливые консультанты в магазине");
        whiteCards.add("Шалава");
        whiteCards.add("Мокрые рейтузы");
        whiteCards.add("Истошный вопль");
        whiteCards.add("Руки-крюки");
        whiteCards.add("Чувство собственного отстоинства");
        whiteCards.add("Пельменный шведский стол");
        whiteCards.add("Бабушкино варенье");

        whiteCards.add("Солнцеликий и всемогущий Нурсултан Назарбаев");
        whiteCards.add("Мозговой слизень");
        whiteCards.add("Покемон Рыгачу");
        whiteCards.add("Муравьи в штанах");
        whiteCards.add("Непорочная зайчатина");
        whiteCards.add("Настолько мощный приход, что отпускает через два дня");
        whiteCards.add("Изощренные пытки");
        whiteCards.add("Корзина гадостей");
        whiteCards.add("Кандибобер");
        whiteCards.add("Застенчивые аниматоры");

        whiteCards.add("Ненастоящий сварщик");
        whiteCards.add("Свиная отбивная с горошком");
        whiteCards.add("Белочка");
        whiteCards.add("Вышедшая из себя улитка");
        whiteCards.add("Похабные мысли в моей голове");
        whiteCards.add("Галстук-бабушка");
        whiteCards.add("Удачное фото на паспорт");
        whiteCards.add("Взгляд на мир сквозь розовые очки");
        whiteCards.add("Ватрушка");
        whiteCards.add("Группа анонимных пингвинов");

        whiteCards.add("Шуба из хомячков");
        whiteCards.add("Бородатый мужик на скейте");
        whiteCards.add("Шлюхи");
        whiteCards.add("Владимирский централ");
        whiteCards.add("Гитлер");
        whiteCards.add("Фонтазирующий унитаз");
        whiteCards.add("Отбеливание ануса");
        whiteCards.add("Сказочный балабол");
        whiteCards.add("Вахтер в общежитии");
        whiteCards.add("Рай в шалаше");

        whiteCards.add("Шалаш с шалавами");
        whiteCards.add("Детское порно для детей");
        whiteCards.add("Регулярные обтирания борщом");
        whiteCards.add("Рыбий жир");
        whiteCards.add("Мудаки");
        whiteCards.add("Дедушкины яйца");
        whiteCards.add("Анальный фистинг как средство от неловкой паузы");
        whiteCards.add("Недержание");
        whiteCards.add("Сайт знакомств");
        whiteCards.add("Восставшие из ада");

        whiteCards.add("Бескрайняя плоть");
        whiteCards.add("Бабки на лавке у подъезда");
        whiteCards.add("Летающий макаронный монстр");
        whiteCards.add("Ворчание");
        whiteCards.add("Капитан Очевидность");
        whiteCards.add("Приличные ругательства");
        whiteCards.add("Надувная кукла Гитлера");
        whiteCards.add("Божий кариес");
        whiteCards.add("Девушка с большим бюстом Ленина");
        whiteCards.add("Коммунизм");

        whiteCards.add("Социальные сети для неудачников");
        whiteCards.add("Торсионные поля");
        whiteCards.add("Сандалии с носками");
        whiteCards.add("Треники");
        whiteCards.add("Римская Империя");
        whiteCards.add("Утипутин");
        whiteCards.add("Божья кара");
        whiteCards.add("Золотой батон бывшего президента");
        whiteCards.add("Противозачаточная внешность");
        whiteCards.add("Моя правая рука");

        whiteCards.add("Неведомая гребаная херня");
        whiteCards.add("Бурные овации");
        whiteCards.add("Страх и ненависть в госдуме");
        whiteCards.add("Всященный орден девственников");
        whiteCards.add("Нервные клетки, убитые алкоголем");
        whiteCards.add("Фунт лиха");
        whiteCards.add("Бабки в очереди за бабками");
        whiteCards.add("Быдло");
        whiteCards.add("Батяня-Вомбат");
        whiteCards.add("Клуб анонимных брошенных женщин");

        whiteCards.add("Святые угодники");
        whiteCards.add("Прокачка девятки");
        whiteCards.add("Ромашковый портвейн");
        whiteCards.add("Суслик-адвокат");
        whiteCards.add("Портянки с ароматом деда");
        whiteCards.add("Покер в переодевание на клоуна");
        whiteCards.add("Нетвердая размазняшка");
        whiteCards.add("Корячащий меч");
        whiteCards.add("Неподкупный полицейский");
        whiteCards.add("Зажигательные бомбы");

        whiteCards.add("Кокаинум");
        whiteCards.add("Кармические мрази");
        whiteCards.add("Мастурбация с помощью бахчевых культур");
        whiteCards.add("Порно с карликами");
        whiteCards.add("Бездарности на нашей сцене");
        whiteCards.add("Уличный музыкант");
        whiteCards.add("Гондон как состояние души");
        whiteCards.add("Пальцы ног");
        whiteCards.add("Отпугивание геев молитвой");
        whiteCards.add("Пробки в Москве");

        whiteCards.add("Мой дедушка");
        whiteCards.add("Порванный презерватив");
        whiteCards.add("Единорог");
        whiteCards.add("Ящик просроченного пива");
        whiteCards.add("Монгольский артхаусный фильм");
        whiteCards.add("Женщина стасомихайловского возраста");
        whiteCards.add("Просроченный говорящий йогурт");
        whiteCards.add("Радиоактивный карась");
        whiteCards.add("Полшестого");
        whiteCards.add("Кришнаиты, бегающие по городу голышом");

        whiteCards.add("Альтернативно одаренный ребенок");
        whiteCards.add("Геноцид с использованием лучших мировых практик");
        whiteCards.add("Полуметровый дилдо");
        whiteCards.add("Убийство на почве глупости");
        whiteCards.add("Робот-гаишник");
        whiteCards.add("Неудачный пук");
        whiteCards.add("Секс на людях)");
        whiteCards.add("Вундервафля");
        whiteCards.add("Метание лилипутов в цель");
        whiteCards.add("Добрые СС-овцы");

        whiteCards.add("Митрополит, отпускающий неприличные намеки");
        whiteCards.add("Лошарики");
        whiteCards.add("Татуировщик, набивающий всем купола");
        whiteCards.add("Немой укор");
        whiteCards.add("Кольцо Всезнайства");
        whiteCards.add("Смерть через сну-сну");
        whiteCards.add("Моих обнаженные фотки в интернете");
        whiteCards.add("Вегетарианская позиция");
        whiteCards.add("Реально нездоровая херня");
        whiteCards.add("Продажные женщины в шкурах");

        whiteCards.add("Блевозавр");
        whiteCards.add("Соски в тиски");
        whiteCards.add("Пластиковый пакет в качестве контрацептива");
        whiteCards.add("Нежное покусывание в правую ягодицу");
        whiteCards.add("Тысяча кусков сыра 'Косичка'");
        whiteCards.add("Таблетки для потенции");
        whiteCards.add("Семечки");
        whiteCards.add("Имитация сарказма");
        whiteCards.add("9 необычных фактов о пенисе");
        whiteCards.add("Мини-динозавны из инкубатора");

        whiteCards.add("Терморектальный криптоанализ");
        whiteCards.add("Ангельская внешность с сучьим характером");
        whiteCards.add("Детский йогурт");
        whiteCards.add("Путевка в жалкие страны");
        whiteCards.add("Полная отдача в счет квартины");
        whiteCards.add("Камера в телефоне");
        whiteCards.add("Повышенная стипендия");
        whiteCards.add("Замороженная шаурма");
        whiteCards.add("Секс по талонам");
        whiteCards.add("Воображаемые друзья");

        whiteCards.add("Гимнастки и актрисы в качестве депутатов");
        whiteCards.add("Сферический конь в вакууме");
        whiteCards.add("Укурка");
        whiteCards.add("Лоботомия шариковой ручкой");
        whiteCards.add("Резкий понос");
        whiteCards.add("Хмурый");
        whiteCards.add("Генератор мурашек");
        whiteCards.add("Жеваный крот");
        whiteCards.add("Демонический смех");
        whiteCards.add("Большой шванцштукер");

        whiteCards.add("Лекарство от зависти");
        whiteCards.add("Ковер на стене");
        whiteCards.add("Капитан Жека Воробьев");
        whiteCards.add("Пирсинг половых органов");
        whiteCards.add("Пикаперы");
        whiteCards.add("Утренняя пробежка на кладбище");
        whiteCards.add("Грибной лимонад");
        whiteCards.add("Граф Вракула");
        whiteCards.add("Запор");
        whiteCards.add("Утренняя смена в стриптиз-клубе");

        whiteCards.add("Опытный генерал диванных войск");
        whiteCards.add("Пацифист, убивающий несогласных");
        whiteCards.add("Порно в переводе Гоблина");
        whiteCards.add("Украшения для сосков");
        whiteCards.add("Интимная стрижка в виде картины Репина");
        whiteCards.add("Падение в унитаз после вставания на него ногами");
        whiteCards.add("Нижнее белье из сыра");
        whiteCards.add("Порядочный политик");
        whiteCards.add("Гопники");
        whiteCards.add("Выживание в шахид-такси");

        whiteCards.add("Стая переползных кротов");
        whiteCards.add("Фальшиво-минетчица");
        whiteCards.add("Принц на белом коне");
        whiteCards.add("Итальянский жиголо");
        whiteCards.add("Танцующий унитаз");
        whiteCards.add("Рвотные позывы");
        whiteCards.add("Заботливая стриптизерша");
        whiteCards.add("Жареная селедка");
        whiteCards.add("Каннибалы-вегани");
        whiteCards.add("Светская религия");

        whiteCards.add("Толстая балерина");
        whiteCards.add("Вши");
        whiteCards.add("Шило в заднице");
        whiteCards.add("Освежитель воздуха с запахом клея");
        whiteCards.add("Шляпа Михаила Боярского");
        whiteCards.add("Ночная гусеница");
        whiteCards.add("Ландышевый пук");
        whiteCards.add("Бег за троллейбусом");
        whiteCards.add("Свидание с овцой соседа");
        whiteCards.add("Выборос адреналина");

        whiteCards.add("Ранняя эякуляция");
        whiteCards.add("Хер без соли");
        whiteCards.add("Возвращение клубного сына");
        whiteCards.add("Сборная Кении по хоккею");
        whiteCards.add("Дуэль возмужденных тушканчиков");
        whiteCards.add("Легендарный сантехник из немецких порнофильмов");
        whiteCards.add("Вскрытие в учебных целях");
        whiteCards.add("Смерть в результате вскрытия");
        whiteCards.add("Картавый лингвист");
        whiteCards.add("Мой фюрер");

        whiteCards.add("Челябинский метеорит");
        whiteCards.add("Дуня Кулакова");
        whiteCards.add("Мазок счастья от шаурмячника");
        whiteCards.add("Секретный соус от шаурмиста");
        whiteCards.add("Геноцид армян");
        whiteCards.add("Облысение после 18");
        whiteCards.add("Падение к кровати");
        whiteCards.add("Умирать от смерти");
        whiteCards.add("Выброс адреналина");
        whiteCards.add("Уран-235");

        whiteCards.add("Дети");
        whiteCards.add("Алкоголизм");

        redCards.add("___ у меня в крови. Это секрет моего успеха в бизнесе");
        redCards.add("Инопланетяне угрожают уничтожить Землю, если им не отдадут ___");
        redCards.add("В рот мне ___!");
        redCards.add("Венец моей карьеры -- ___");
        redCards.add("___ -- современное оружие революции");
        redCards.add("___ -- вот чем должна гордиться Россия!");
        redCards.add("___ -- лучший способ воспитания ребенка");
        redCards.add("Кто я? ___ или право имею");
        redCards.add("Хорошая зарплата -- это когда хватает не только на еду, но и на ___");
        redCards.add("Если долго смотреть в зеркало, можно увидеть ___");

        redCards.add("На всероссийском фестивале шансона в этом году победил ___");
        redCards.add("Надежный способ победить на парламентских выборах -- ___");
        redCards.add("В следующем веке ___ заменит людям секс");
        redCards.add("Впервые на арене цирка! ___!");
        redCards.add("На уроках труда в Китае дети делают смартфоны, телевизоры и ___");
        redCards.add("Классное сочинение: 'Герасим и ___'");
        redCards.add("Любимый фокус Михаила Боярского -- доставание ___ из шляпы");
        redCards.add("Первыми словами Гагарина в космосе были: 'Смотрите! Там ___!'");
        redCards.add("___ обязательно возьмут в экспедицию на Марс");
        redCards.add("Можно забыть о матери, но свой первый ___ ты будешь помнить всегда");

        redCards.add("Мужчина, который не любит ___, неинтересен женщинам");
        redCards.add("Да пребудет с тобой ___");
        redCards.add("С подчиненными я использую метод кнута и ___");
        redCards.add("___ помогает мне защищаться от хулиганов");
        redCards.add("___ -- мое основное накопление на пенсию");
        redCards.add("Нанотехнологии в действии! Сколковские ученые представили новую разработку: ___");
        redCards.add("В новом фильме Безруков сыграет ___");
        redCards.add("Береги ___ смолоду!");
        redCards.add("В детстве я хотел стать космонавтом, а стал ___");
        redCards.add("Увидеть во сне ___ -- это к несчастью");

        redCards.add("Кто-то выпрашивает цветочки, а я ___");
        redCards.add("Если бы я был супергероем, то меня бы звали Человек-___");
        redCards.add("Выяснилось, что люди, которые знакомятся в интернете, ищут одного -- ___");
        redCards.add("Ешь, молись, ___");
        redCards.add("___ после бани -- омолаживает");
        redCards.add("Родила царица в ночь, не то сына, не то ___");
        redCards.add("Основная достопримечательность города Когалыма -- ___");
        redCards.add("Каждому человеку для секса нужен ___");
        redCards.add("Селфи с ___ сделало меня популярным");
        redCards.add("Почти все наши налоги тратятся на одно -- ___");

        redCards.add("В следующем году ___ станет обязательным предметом школьной программы");
        redCards.add("Формула вечной жизни: ___ и немного алкоголя каждый день");
        redCards.add("Британские ученые выяснили, что ___ -- это главная причина алкоголизма");
        redCards.add("Лучшее средство от комаров -- ___");
        redCards.add("Не знаете, чем заняться в отпуске? Попробуйте ___");
        redCards.add("На новой выставке современного искусства главным экспонатом стал ___");
        redCards.add("Здорово было бы, если бы вместо надписи 'Конец' в фильмах показывали ___");
        redCards.add("Лучший подарок на день рождения -- это ___");
        redCards.add("Узнать, девочка перед вами или мальчик, поможет ___");
        redCards.add("Бесконечно можно смотреть на три вещи: огонь, воду и ___");

        redCards.add("Алкоголизм -- просто обратная сторона ___");
        redCards.add("Ученые открыли новое заболевание. В переводе с латыни его название звучит как ___");
        redCards.add("Что мне мешает бегать по утрам? ___ мешает");
        redCards.add("Что обязательно нужно брать на спортивную тренировку?");
        redCards.add("Выбор между мужем и ___ всегда труден для каждой уважающей себя женщины");
        redCards.add("В гостях хорошо, а ___ лучше");
        redCards.add("То ли девочка, а то ли ___");
        redCards.add("По утрам я люблю встать, потянуться и позавтракать ___");
        redCards.add("Мой единственный недостаток -- это ___");
        redCards.add("Президент будет править вечно. Его секрет -- ___");

        redCards.add("За кольцо с бриллиантами не соглашайтесь на меньшее, чем ___");
        redCards.add("Очередной катаклизм в Индонезии -- ___");
        redCards.add("День рождения -- отличный повод для ___");
        redCards.add("Шла Саша по шоссе и сосала ___");
        redCards.add("Результаты моего теста на совместимость говорят, что лучшим партнером для меня будет ___");
        redCards.add("Продвинуться по карьерной лестнице вам поможет ___ в качестве подарка боссу");
        redCards.add("___ с утра -- гарантированный секс вечером");
        redCards.add("Деда Мороза не существует, на самом деле он -- ___");
        redCards.add("Каждый мужчина думает ___ 40 раз в день");
        redCards.add("___ -- по-прежнему считается самым надежным противозачаточным средством");

        redCards.add("В мире много хороших людей, но я всегда общаюсь с ___");
        redCards.add("Сегодня, дети, мы будем забивать гвозди с помощью ___");
        redCards.add("Смех без причины -- признак ___");
        redCards.add("Зоопарк потерял ___, звери объявили голодовку");
        redCards.add("Живешь только однажды, не отказывай себе в ___");
        redCards.add("На что похожа личная жизнь");
        redCards.add("Угрожая людям с ___ в руке, вы похожи на идиота");
        redCards.add("То, что в мире называют кризисом, в России называют ___");
        redCards.add("Собери 100 крышечек от пива и сделай из них ___");
        redCards.add("Про дураков и дороги все в курсе, но есть и третья проблема -- ___");

        redCards.add("Я бы любил свою работу, если бы ___ всегдя было рядом с моим столом");
        redCards.add("Взрослая жизнь -- это ___");
        redCards.add("Ради ___ я отказался от участия в оргии");
        redCards.add("На первом свидании не нужно слов -- подари ___");
        redCards.add("Проблему низкой рождаемости в России можно решить с помощью ___");
        redCards.add("Приходите! Моя секретарша сделает вам ___");
        redCards.add("Моя несбыточная мечта -- это ___");
        redCards.add("В конце всех отношений только одно -- ___");
        redCards.add("У каждого свой ___ в шкафу");
        redCards.add("Последствия применения ___ могут быть непредсказуемы");

        redCards.add("Здравствуйте, доктор. У меня новая проблема со здоровьем -- ___. Что посоветуете?");
        redCards.add("Традиционный секс мне приелся, теперь я беру в постель ___");
        redCards.add("Мыши плакали, кололись, но продолжали грызть ___");
        redCards.add("Вот подарили бы мне ___ на кассе в супермаркете!");
        redCards.add("Чтобы стать моделью, нужно иметь отличную фигуру и ___");
        redCards.add("Когда мужчины не понимают по-хорошему, в ход идет ___");
        redCards.add("Лучшие друзья девушек -- это ___");
        redCards.add("Любой супергероя боится только одного -- ___");
        redCards.add("___ разбудит лучше поцелуя");
        redCards.add("___ -- то, что каждый хочет получить в подарок, но не каждый может");

        redCards.add("Как прожить месяц без ___ и не лишиться рассудка");
        redCards.add("Кому и ___ невеста");
        redCards.add("___ -- лучший друг женских ртов");
        redCards.add("Новый бизнес: срочная доставка ___");
        redCards.add("Любовь не купишь, но можно купить ___, а это почти то же самое");
        redCards.add("Любовь за деньги -- это то же самое, что ___ бесплатно");
        redCards.add("Сидит девица в темнице, а ___ на улице");
        redCards.add("Всем странным поступкам есть одно объяснение -- ___");
        redCards.add("Лучше секса может быть только ___");
        redCards.add("По статистике, каждая девушка до 18 лет страдает ___");

        redCards.add("Не имей 100 рублей, а имей 100 ___");
        redCards.add("В российских лесах обнаружено новое животное -- ___");
        redCards.add("Переходи на темную сторону, а нас есть ___");
        redCards.add("Карл у Клары украл кораллы, а Клара и Карла украла ___");
        redCards.add("Человек, который изобрел ___, должен быть жестоко наказан");
        redCards.add("От ___ еще никто не умирал");
        redCards.add("Клуб анонимных ___ набирает новую группу");
        redCards.add("___ помогает уйти от мрачной действительности в мир иллюзий");
        redCards.add("");
        redCards.add("");

        Collections.shuffle(whiteCards);
        Collections.shuffle(redCards);

        // Настройка HTTP сервера
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/state", new StateHandler());
        server.createContext("/api/play", new PlayHandler());
        server.createContext("/api/choose", new ChooseHandler());
        server.createContext("/api/start", new StartHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");
    }

    static class GameState {
        boolean gameStarted = false;
        String currentRedCard = "";
        String currentRoundLeader = "";
        Map<String, String> playedCards = new HashMap<>();
        String winningCard = "";
        String winningPlayer = "";
        List<String> playerOrder = new ArrayList<>();
        int currentRound = 0;
    }

    static class Player {
        String name;
        List<String> cards = new ArrayList<>();
        int score = 0;
        boolean isLeader = false;

        Player(String name) {
            this.name = name;
        }

        void dealCards(List<String> deck, int count) {
            for (int i = 0; i < count; i++) {
                if (!deck.isEmpty()) {
                    cards.add(deck.remove(0));
                }
            }
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
        // Обработка OPTIONS-запроса (preflight)
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 200, "");
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String name = new Scanner(exchange.getRequestBody()).nextLine();
            String response;

            if (players.containsKey(name)) {
                // Игрок существует
                response = "{\"status\":\"existing\", \"name\":\"" + name + "\"}";
            } else {
                // Новый игрок
                Player player = new Player(name);
                player.dealCards(whiteCards, CARDS_PER_PLAYER);
                players.put(name, player);
                response = "{\"status\":\"new\", \"name\":\"" + name + "\"}";
            }

            sendResponse(exchange, 200, response);
        }
    }

    static class StateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
                // Обработка OPTIONS-запроса (preflight)
                    if ("OPTIONS".equals(exchange.getRequestMethod())) {
                        sendResponse(exchange, 200, "");
                        return;
                    }

            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            // Подготовка состояния игры для отправки клиенту
            Map<String, Object> state = new HashMap<>();
            state.put("gameStarted", gameState.gameStarted);
            state.put("currentRedCard", gameState.currentRedCard);
            state.put("currentRoundLeader", gameState.currentRoundLeader);
            state.put("playedCards", gameState.playedCards);
            state.put("winningCard", gameState.winningCard);
            state.put("winningPlayer", gameState.winningPlayer);

            // Информация об игроках
            Map<String, Integer> scores = new HashMap<>();
            players.forEach((name, player) -> scores.put(name, player.score));
            state.put("scores", scores);

            // Информация о текущем игроке (из параметра запроса)
            String playerName = exchange.getRequestURI().getQuery().split("=")[1];
            Player player = players.get(playerName);
            if (player != null) {
                state.put("myCards", player.cards);
                state.put("isLeader", player.isLeader);
                state.put("myName", player.name);
            }

            String response = new Gson().toJson(state);
            sendResponse(exchange, 200, response);
        }
    }

    static class PlayHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
                // Обработка OPTIONS-запроса (preflight)
                    if ("OPTIONS".equals(exchange.getRequestMethod())) {
                        sendResponse(exchange, 200, "");
                        return;
                    }

            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String[] parts = new Scanner(exchange.getRequestBody()).nextLine().split(":");
            String playerName = parts[0];
            String cardText = parts[1];

            Player player = players.get(playerName);
            if (player != null && player.cards.contains(cardText) && !player.isLeader) {
                player.cards.remove(cardText);
                gameState.playedCards.put(playerName, cardText);
                sendResponse(exchange, 200, "{\"status\":\"ok\"}");
            } else {
                sendResponse(exchange, 400, "{\"status\":\"error\"}");
            }
        }
    }

    static class ChooseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
                // Обработка OPTIONS-запроса (preflight)
                    if ("OPTIONS".equals(exchange.getRequestMethod())) {
                        sendResponse(exchange, 200, "");
                        return;
                    }

            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String[] parts = new Scanner(exchange.getRequestBody()).nextLine().split(":");
            String leaderName = parts[0];
            String winningCard = parts[1];

            if (gameState.currentRoundLeader.equals(leaderName)) {
                // Находим игрока, который сыграл эту карту
                String winningPlayer = "";
                for (Map.Entry<String, String> entry : gameState.playedCards.entrySet()) {
                    if (entry.getValue().equals(winningCard)) {
                        winningPlayer = entry.getKey();
                        break;
                    }
                }

                if (!winningPlayer.isEmpty()) {
                    players.get(winningPlayer).score++;
                    gameState.winningCard = winningCard;
                    gameState.winningPlayer = winningPlayer;

                    // Раздаем новую карту игроку
                    if (!whiteCards.isEmpty()) {
                        players.get(winningPlayer).cards.add(whiteCards.remove(0));
                    }

                    // Переход к следующему раунду
                    nextRound();
                    sendResponse(exchange, 200, "{\"status\":\"ok\"}");
                    return;
                }
            }

            sendResponse(exchange, 400, "{\"status\":\"error\"}");
        }
    }

    static class StartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            if (players.size() >= 2) {
                startGame();
                sendResponse(exchange, 200, "{\"status\":\"ok\"}");
            } else {
                sendResponse(exchange, 400, "{\"status\":\"not enough players\"}");
            }
        }
    }

    private static void startGame() {
        gameState.gameStarted = true;
        gameState.playerOrder = new ArrayList<>(players.keySet());
        Collections.shuffle(gameState.playerOrder);
        gameState.currentRound = 0;
        nextRound();
    }

    private static void nextRound() {
        gameState.currentRound++;

        // Удаляем сыгранные карты из общего списка белых карт
        for (String playedCard : gameState.playedCards.values()) {
            whiteCards.remove(playedCard);
        }

        // Сброс состояния раунда
        gameState.playedCards.clear();
        gameState.winningCard = "";
        gameState.winningPlayer = "";

        // Выбор нового ведущего
        int leaderIndex = (gameState.currentRound - 1) % gameState.playerOrder.size();
        String newLeader = gameState.playerOrder.get(leaderIndex);
        gameState.currentRoundLeader = newLeader;

        // Сброс флага ведущего у всех и установка новому
        players.values().forEach(p -> p.isLeader = false);
        players.get(newLeader).isLeader = true;

        // Выбор красной карты
        if (!redCards.isEmpty()) {
            gameState.currentRedCard = redCards.remove(0);
        } else {
            gameState.currentRedCard = "Игра завершена!";
            gameState.gameStarted = false;
        }

        // Раздача карт игрокам, у которых меньше 10
        players.values().forEach(p -> {
            if (p.cards.size() < CARDS_PER_PLAYER && !whiteCards.isEmpty()) {
                p.dealCards(whiteCards, CARDS_PER_PLAYER - p.cards.size());
            }
        });
    }

    private static void sendResponse(HttpExchange exchange, int code, String response) throws IOException {
        // Настройки CORS
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "*");
        exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
        
        // Для preflight запросов
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Основные заголовки
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}