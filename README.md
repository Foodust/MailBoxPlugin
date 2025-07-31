# 우체통 플러그인

Minecraft 서버용 우체통 플러그인입니다.

## 기능

- 각 플레이어는 개인 우체통을 소유합니다
- 우체통 데이터는 플레이어별로 개별 파일에 저장됩니다 (`mail/{UUID}.yml`)
- 플레이어는 우체통에 직접 아이템을 넣을 수 없습니다
- 명령어를 통해서만 다른 플레이어의 우체통에 아이템을 전송할 수 있습니다

## 데이터 저장

플레이어의 우체통 데이터는 다음 경로에 저장됩니다:
```
plugins/MailBoxPlugin/mail/{플레이어UUID}.yml
```

## 명령어

우체통 관련 명령어를 통해 아이템을 전송하고 관리할 수 있습니다.  
 > /우체통 열기  
 > 플레이어가 자신의 우체통을 엽니다.

## API 사용법

다른 플러그인에서 MailBoxPlugin의 API를 사용하여 아이템을 전송할 수 있습니다.

### 의존성 추가

plugin.yml에 다음을 추가하세요:
```yaml
depend: [MailBoxPlugin]
```
또는
```yaml
softdepend: [MailBoxPlugin]
```

### API 사용 예제

```java
import org.foodust.mailBoxPlugin.api.MailBoxAPI;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

// API 인스턴스 가져오기
MailBoxAPI api = MailBoxAPI.getInstance();

// 플레이어에게 아이템 보내기
Player targetPlayer = ...;
ItemStack item = ...;
boolean success = api.sendItem(targetPlayer, item);

// 발신자 이름과 함께 보내기
api.sendItem(targetPlayer, item, "관리자");

// UUID로 보내기
UUID targetUUID = ...;
api.sendItem(targetUUID, item, "시스템");

// 여러 아이템 한번에 보내기
ItemStack[] items = ...;
api.sendItems(targetUUID, items, "이벤트");

// 비동기로 보내기
api.sendItemAsync(targetUUID, item).thenAccept(result -> {
    if (result) {
        // 성공
    } else {
        // 실패 (우체통이 가득 참)
    }
});

// 우체통 공간 확인
if (api.hasSpace(targetUUID)) {
    // 공간이 있음
}

// 우편 개수 확인
int count = api.getMailCount(targetUUID);
```