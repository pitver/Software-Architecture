## Шаг 1: Создание образов подов
```
kubectl run front-end-app --image=nginx --labels role=front-end --expose --port 80
kubectl run back-end-api-app --image=nginx --labels role=back-end-api --expose --port 80
kubectl run admin-front-end-app --image=nginx --labels role=admin-front-end --expose --port 80
kubectl run admin-back-end-api-app --image=nginx --labels role=admin-back-end-api --expose --port 80
```
## Шаг 2: Проверка запущенных подов и меток

kubectl get pods --show-labels
```
NAME                     READY   STATUS    RESTARTS   AGE   LABELS                 
admin-back-end-api-app   1/1     Running   0          23m   role=admin-back-end-api
admin-front-end-app      1/1     Running   0          23m   role=admin-front-end   
back-end-api-app         1/1     Running   0          23m   role=back-end-api      
front-end-app            1/1     Running   0          23m   role=front-end 
```
## Шаг 3: Создание сетевых политик
kubectl apply -f network-policies.yaml

## Шаг 4: Проверка работы сетевых политик
kubectl get networkpolicy
```
admin-api-allow       role=admin-front-end   9m49s
default-deny-all      <none>                 72s  
non-admin-api-allow   role=front-end         9m49s

```
kubectl get pods --show-labels
```
NAME                     READY   STATUS    RESTARTS   AGE     LABELS
admin-back-end-api-app   1/1     Running   0          12m     role=admin-back-end-api
admin-front-end-app      1/1     Running   0          12m     role=admin-front-end
back-end-api-app         1/1     Running   0          12m     role=back-end-api
front-end-app            1/1     Running   0          12m     role=front-end

```
