# MailBox Plugin

Minecraft Spigot/Paper 서버용 우체통 플러그인입니다. 플레이어 간 아이템을 안전하게 전송할 수 있는 시스템을 제공합니다.

## 특징

- 📦 오프라인 플레이어에게도 아이템 전송 가능
- 💾 자동 저장 기능 (플레이어별 개별 파일 `mail/{UUID}.yml`)
- 🎨 직관적인 GUI 인터페이스
- ⚡ 비동기 처리 지원
- 🔒 안전한 아이템 전송 (우체통에 직접 아이템을 넣을 수 없음)

## 요구사항

- Minecraft 1.21+
- Spigot/Paper 서버
- Java 21+

## 설치

1. 최신 릴리즈에서 JAR 파일을 다운로드합니다.
2. 서버의 `plugins` 폴더에 JAR 파일을 넣습니다.
3. 서버를 재시작합니다.

## 명령어

| 명령어 | 설명 |
|--------|------|
| `/우체통 열기` | 우체통 GUI를 엽니다 |
| `/우체통 리로드` | 플러그인 설정을 리로드합니다 |

## API 사용법

### 의존성 추가

#### Maven
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.Foodust</groupId>
    <artifactId>MailBoxPlugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

#### Gradle
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.Foodust:MailBoxPlugin:1.0-SNAPSHOT'
}
```

#### plugin.yml
```yaml
depend: [MailBoxPlugin]
# 또는
softdepend: [MailBoxPlugin]
```

### API 예제

```java
import org.foodust.mailBoxPlugin.api.MailBoxAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.UUID;

public class ExamplePlugin {
    
    public void sendItemToPlayer(Player sender, Player target, ItemStack item) {
        // API 인스턴스 가져오기
        MailBoxAPI api = MailBoxAPI.getInstance();
        
        // 아이템 전송
        boolean success = api.sendItem(target, item, sender.getName());
        
        if (success) {
            sender.sendMessage("아이템을 성공적으로 전송했습니다!");
        } else {
            sender.sendMessage("우체통이 가득 찼습니다!");
        }
    }
    
    public void sendMultipleItems(UUID targetUUID, ItemStack[] items) {
        MailBoxAPI api = MailBoxAPI.getInstance();
        
        // 여러 아이템 한번에 전송
        api.sendItems(targetUUID, items, "시스템");
    }
    
    public void sendItemAsync(Player target, ItemStack item) {
        MailBoxAPI api = MailBoxAPI.getInstance();
        
        // 비동기로 아이템 전송
        api.sendItemAsync(target.getUniqueId(), item, "익명")
            .thenAccept(success -> {
                if (success) {
                    // 전송 성공 처리
                }
            });
    }
    
    public void checkMailBox(Player player) {
        MailBoxAPI api = MailBoxAPI.getInstance();
        
        // 우체통 공간 확인
        if (api.hasSpace(player.getUniqueId())) {
            // 우체통에 공간이 있음
        }
        
        // 메일 개수 확인
        int mailCount = api.getMailCount(player.getUniqueId());
        player.sendMessage("우체통에 " + mailCount + "개의 메일이 있습니다.");
    }
    
    public void clearMailBox(Player player) {
        MailBoxAPI api = MailBoxAPI.getInstance();
        
        // 우체통 비우기
        boolean cleared = api.clearMailBox(player.getUniqueId());
        if (cleared) {
            player.sendMessage("우체통을 비웠습니다.");
        }
    }
}
```

### 주요 API 메소드

#### 아이템 전송
```java
// 기본 전송
boolean sendItem(UUID targetUUID, ItemStack item)
boolean sendItem(UUID targetUUID, ItemStack item, String senderName)
boolean sendItem(Player target, ItemStack item)
boolean sendItem(Player target, ItemStack item, String senderName)

// 여러 아이템 전송
boolean sendItems(UUID targetUUID, ItemStack[] items)
boolean sendItems(UUID targetUUID, ItemStack[] items, String senderName)

// 비동기 전송
CompletableFuture<Boolean> sendItemAsync(UUID targetUUID, ItemStack item)
CompletableFuture<Boolean> sendItemAsync(UUID targetUUID, ItemStack item, String senderName)
```

#### 우체통 관리
```java
// 우체통 공간 확인
boolean hasSpace(UUID playerUUID)

// 메일 개수 확인
int getMailCount(UUID playerUUID)

// 우체통 비우기
boolean clearMailBox(UUID playerUUID)
```

## 데이터 저장

플레이어의 우체통 데이터는 다음 경로에 자동으로 저장됩니다:
```
plugins/MailBoxPlugin/mail/{플레이어UUID}.yml
```

## 빌드

프로젝트를 직접 빌드하려면:

```bash
git clone https://github.com/Foodust/MailBoxPlugin.git
cd MailBoxPlugin
./gradlew build
```

빌드된 JAR 파일은 `build/libs/` 폴더에 생성됩니다.

## 기여

기여는 언제나 환영합니다! Pull Request를 보내주세요.

1. 프로젝트를 Fork합니다
2. 기능 브랜치를 생성합니다 (`git checkout -b feature/AmazingFeature`)
3. 변경사항을 커밋합니다 (`git commit -m 'Add some AmazingFeature'`)
4. 브랜치에 Push합니다 (`git push origin feature/AmazingFeature`)
5. Pull Request를 생성합니다

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 문의

문제가 발생하거나 제안사항이 있으시면 [Issues](https://github.com/[your-username]/MailBoxPlugin/issues)에 등록해주세요.

## 작성자

- **Foodust** - *Initial work*