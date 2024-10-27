# Инструкция по созданию сервис-аккаунтов, ролей и привязок в Kubernetes

## 1. Создание сервис-аккаунтов

Создайте три сервис-аккаунта для разных уровней доступа:

```bash
kubectl create serviceaccount viewer-user -n default
kubectl create serviceaccount config-user -n default
kubectl create serviceaccount admin-user -n default
```

## 2. Создание ролей
Создайте роли для каждого типа пользователя, создавая YAML-файлы для каждой роли.

### Роль Viewer
Создайте файл role-viewer.yaml
```
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: default
  name: viewer
rules:
  - apiGroups: [""]
    resources: ["pods", "services", "deployments"]
    verbs: ["get", "list"]
```
### Роль Configurer
Создайте файл role-configurer.yaml
```
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: default
  name: configurer
rules:
  - apiGroups: [""]
    resources: ["pods", "services", "deployments"]
    verbs: ["get", "list", "create", "update"]
```
### Роль Admin
Создайте файл role-admin.yaml
```
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: admin
rules:
  - apiGroups: [""]
    resources: ["*"]
    verbs: ["*"]
  - apiGroups: ["rbac.authorization.k8s.io"]
    resources: ["roles", "rolebindings", "clusterroles", "clusterrolebindings"]
    verbs: ["*"]
  - apiGroups: ["metrics.k8s.io"]
    resources: ["pods", "nodes"]
    verbs: ["get", "list"]
```
## 3. Применение ролей
Примените созданные файлы с ролями
```
kubectl apply -f role-viewer.yaml
kubectl apply -f role-configurer.yaml
kubectl apply -f role-admin.yaml
```
## 4. Проверка созданных ролей
Убедитесь, что роли созданы успешно
```
kubectl get roles -n default
kubectl get clusterroles | grep admin
```
## 5. Создание привязок
Cоздайте привязки для связывания сервис-аккаунтов с соответствующими ролями

### Привязка для viewer-user
Создайте файл rolebinding-viewer.yaml
```
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: viewer-binding
  namespace: default
subjects:
- kind: ServiceAccount
  name: viewer-user
  namespace: default
roleRef:
  kind: Role
  name: viewer
  apiGroup: rbac.authorization.k8s.io

```

### Привязка для config-user
Создайте файл rolebinding-configurer.yaml
```
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: configurer-binding
  namespace: default
subjects:
- kind: ServiceAccount
  name: config-user
  namespace: default
roleRef:
  kind: Role
  name: configurer
  apiGroup: rbac.authorization.k8s.io

```

### Привязка для admin-user
Создайте файл clusterrolebinding-admin.yaml
```
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-binding
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: default
roleRef:
  kind: ClusterRole
  name: admin
  apiGroup: rbac.authorization.k8s.io

```

## 6. Применение привязок
Примените созданные файлы с привязками
```
kubectl apply -f rolebinding-viewer.yaml
kubectl apply -f rolebinding-configurer.yaml
kubectl apply -f clusterrolebinding-admin.yaml

```
## 7. Проверка привязок
```
kubectl get rolebindings -n default
kubectl get clusterrolebindings | grep admin
```
### Результат


kubectl get rolebindings -n default
```
NAME                 ROLE              AGE
configurer-binding   Role/configurer   16m
viewer-binding       Role/viewer       16m
```

kubectl get clusterrolebindings | grep admin
```
admin-binding                                          ClusterRole/admin                                                                  16m
cluster-admin                                          ClusterRole/cluster-admin                                                          28m
kubernetes-dashboard                                   ClusterRole/cluster-admin                                                          28m
minikube-rbac                                          ClusterRole/cluster-admin                                                          28m

```